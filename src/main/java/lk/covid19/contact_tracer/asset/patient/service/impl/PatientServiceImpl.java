package lk.covid19.contact_tracer.asset.patient.service.impl;


import lk.covid19.contact_tracer.asset.attempt.entity.Attempt;
import lk.covid19.contact_tracer.asset.attempt.service.AttemptService;
import lk.covid19.contact_tracer.asset.patient.dao.PatientDao;
import lk.covid19.contact_tracer.asset.patient.entity.Patient;
import lk.covid19.contact_tracer.asset.patient.entity.enums.DeadLive;
import lk.covid19.contact_tracer.asset.patient.service.PatientService;
import lk.covid19.contact_tracer.util.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig( cacheNames = "patient" )
public class PatientServiceImpl implements PatientService {

    private final PatientDao patientDao;
    private final CommonService commonService;
    private final AttemptService attemptService;

    @Cacheable
    public Page< Patient > findAllPageable(Pageable pageable) {
        return patientDao.findAll(pageable);
    }

    @Cacheable
    public List< Patient > findAll() {
        return patientDao.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Cacheable
    public Patient findById(Integer id) {
        return patientDao.getById(id);
    }

    @Caching( evict = {@CacheEvict( value = "patient", allEntries = true )},
        put = {@CachePut( value = "patient", key = "#patient.id" )} )
    public Patient persist(Patient patient) {
        if ( patient.getId() == null ) {
            patientPersist(patient);
        }
        patient.setMobile(patient.getMobile() != null ?
                              commonService.phoneNumberLengthValidator(patient.getMobile()) : null);
        patient.setName(commonService.stringCapitalize(patient.getName()));
        Patient patientDb = patientDao.save(patient);
        if ( patient.getId() == null ) {
            Attempt attempt = Attempt.builder()
                .identifiedDate(patient.getIdentifiedDate())
                .patient(patientDb)
                .build();
            attemptService.persist(attempt);
        } else {
            if ( patient.getIdentifiedDate() != null ) {
                Attempt attempt = Attempt.builder()
                    .identifiedDate(patient.getIdentifiedDate())
                    .patient(patientDb)
                    .build();
                attemptService.persist(attempt);
            }
        }
        return patientDb;
    }

    @Caching( evict = {@CacheEvict( value = "patient", allEntries = true )},
        put = {@CachePut( value = "patient", key = "'#patient.id'" )} )
    public List< Patient > persistList(List< Patient > patients) {
        HashSet< Patient > patientHashSet = new HashSet<>();

        for ( Patient patient : patients ) {
            if ( patient.getNic() != null ) {
                Patient patientDb = patientDao.findByNic(patient.getNic());
                if ( patientDb != null ) {
                    Attempt attemptDb = attemptService.findByPatientAndIdentifiedDate(patientDb,
                                                                                      patient.getAttempt().getIdentifiedDate());
                    if ( attemptDb != null ) {
                        patientDb.setActiveInactive(DeadLive.DEAD);
                    } else {
                        attemptDb = patient.getAttempt();
                        attemptDb.setPatient(patientDb);
                        attemptService.persist(attemptDb);
                        patientDb.setActiveInactive(DeadLive.LIVE);
                    }
                    if ( !patientDb.getMobile().equals(patient.getMobile()) ) {
                        patientDb.setMobile(patient.getMobile());
                    }
                    patientHashSet.add(patientDao.save(patientDb));
                }
                continue;
            }
            try {
                Attempt attempt = patient.getAttempt();
                patientPersist(patient);
                Patient patientDb = patientDao.save(patient);
                attempt.setPatient(patientDb);
                attemptService.persist(attempt);
                patientDb.setActiveInactive(DeadLive.LIVE);
                patientHashSet.add(patientDb);
            } catch ( Exception e ) {
                patient.setActiveInactive(DeadLive.DEAD);
                patient.setRemarks(e.getCause().getCause().getMessage());
                patientHashSet.add(patient);
            }
        }
        return new ArrayList<>(patientHashSet);
    }

    private void patientPersist(Patient patient) {
        patient.setDeadLive(DeadLive.LIVE);
        if ( patientDao.findFirstByOrderByIdDesc() == null ) {
            patient.setCode("LKCP" + commonService.numberAutoGen(null).toString());
        } else {
            String previousCode = patientDao.findFirstByOrderByIdDesc().getCode().substring(4);
            patient.setCode("LKCP" + commonService.numberAutoGen(previousCode).toString());
        }
    }

    public boolean delete(Integer id) {
        //this was prohibited method
        return false;
    }

    @Cacheable
    public List< Patient > search(Patient patient) {
        ExampleMatcher matcher = ExampleMatcher
            .matching()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example< Patient > patientExample = Example.of(patient, matcher);
        return patientDao.findAll(patientExample);
    }

    @Cacheable
    public Patient findByNic(String nic) {
        return patientDao.findByNic(nic);
    }

    @Cacheable
    public Patient findLastPatient() {
        return patientDao.findFirstByOrderByIdDesc();
    }

    @Override
    public List< Patient > findByAttemptIdentifiedDateRange(LocalDate startDate, LocalDate endDate) {
        HashSet< Patient > patients = new HashSet<>();
        for ( Attempt attempt : attemptService.findByIdentifiedDateIsBetween(startDate, endDate) ) {
            patients.add(patientDao.getById(attempt.getPatient().getId()));
        }
        return new ArrayList<>(patients);
    }
}
