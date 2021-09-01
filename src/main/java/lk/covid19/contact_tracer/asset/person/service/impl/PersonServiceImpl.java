package lk.covid19.contact_tracer.asset.person.service.impl;


import lk.covid19.contact_tracer.asset.attempt.entity.Attempt;
import lk.covid19.contact_tracer.asset.attempt.service.AttemptService;
import lk.covid19.contact_tracer.asset.person.dao.PersonDao;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.asset.person.entity.enums.PersonStatus;
import lk.covid19.contact_tracer.asset.person.service.PersonService;
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
public class PersonServiceImpl implements PersonService {

    private final PersonDao personDao;
    private final CommonService commonService;
    private final AttemptService attemptService;

    @Cacheable
    public Page< Person > findAllPageable(Pageable pageable) {
        return personDao.findAll(pageable);
    }

    @Cacheable
    public List< Person > findAll() {
        return personDao.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Cacheable
    public Person findById(Integer id) {
        return personDao.getById(id);
    }

    @Caching( evict = {@CacheEvict( value = "patient", allEntries = true )},
        put = {@CachePut( value = "patient", key = "#person.id" )} )
    public Person persist(Person person) {
        if ( person.getId() == null ) {
            patientPersist(person);
        }
        person.setMobile(person.getMobile() != null ?
                             commonService.phoneNumberLengthValidator(person.getMobile()) : null);
        person.setName(commonService.stringCapitalize(person.getName()));
        Person personDb = personDao.save(person);
        if ( person.getId() == null ) {
            Attempt attempt = Attempt.builder()
                .identifiedDate(person.getIdentifiedDate())
                .person(personDb)
                .build();
            attemptService.persist(attempt);
        } else {
            if ( person.getIdentifiedDate() != null ) {
                Attempt attempt = Attempt.builder()
                    .identifiedDate(person.getIdentifiedDate())
                    .person(personDb)
                    .build();
                attemptService.persist(attempt);
            }
        }
        return personDb;
    }

    @Caching( evict = {@CacheEvict( value = "patient", allEntries = true )},
        put = {@CachePut( value = "patient", key = "'#patient.id'" )} )
    public List< Person > persistList(List< Person > people) {
        HashSet< Person > personHashSet = new HashSet<>();

        for ( Person person : people ) {
            if ( person.getNic() != null ) {
                Person personDb = personDao.findByNic(person.getNic());
                if ( personDb != null ) {
                    Attempt attemptDb = attemptService.findByPatientAndIdentifiedDate(personDb,
                                                                                      person.getAttempt().getIdentifiedDate());
                    if ( attemptDb != null ) {
                        personDb.setActiveInactive(PersonStatus.DEAD);
                    } else {
                        attemptDb = person.getAttempt();
                        attemptDb.setPerson(personDb);
                        attemptService.persist(attemptDb);
                      personDb.setActiveInactive(PersonStatus.GENERAL);
                    }
                    if ( !personDb.getMobile().equals(person.getMobile()) ) {
                        personDb.setMobile(person.getMobile());
                    }
                    personHashSet.add(personDao.save(personDb));
                }
                continue;
            }
            try {
                Attempt attempt = person.getAttempt();
                patientPersist(person);
                Person personDb = personDao.save(person);
                attempt.setPerson(personDb);
              attemptService.persist(attempt);
              personDb.setActiveInactive(PersonStatus.GENERAL);
              personHashSet.add(personDb);
            } catch ( Exception e ) {
                person.setActiveInactive(PersonStatus.DEAD);
                person.setRemarks(e.getCause().getCause().getMessage());
                personHashSet.add(person);
            }
        }
        return new ArrayList<>(personHashSet);
    }

    private void patientPersist(Person person) {
      person.setPersonStatus(PersonStatus.GENERAL);
        if ( personDao.findFirstByOrderByIdDesc() == null ) {
            person.setCode("LKCP" + commonService.numberAutoGen(null).toString());
        } else {
            String previousCode = personDao.findFirstByOrderByIdDesc().getCode().substring(4);
            person.setCode("LKCP" + commonService.numberAutoGen(previousCode).toString());
        }
    }

    public boolean delete(Integer id) {
        //this was prohibited method
        return false;
    }

    @Cacheable
    public List< Person > search(Person person) {
        ExampleMatcher matcher = ExampleMatcher
            .matching()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example< Person > patientExample = Example.of(person, matcher);
        return personDao.findAll(patientExample);
    }

    @Cacheable
    public Person findByNic(String nic) {
        return personDao.findByNic(nic);
    }

    @Cacheable
    public Person findLastPatient() {
        return personDao.findFirstByOrderByIdDesc();
    }

    @Override
    public List< Person > findByAttemptIdentifiedDateRange(LocalDate startDate, LocalDate endDate) {
        HashSet< Person > people = new HashSet<>();
        for ( Attempt attempt : attemptService.findByIdentifiedDateIsBetween(startDate, endDate) ) {
            people.add(personDao.getById(attempt.getPerson().getId()));
        }
        return new ArrayList<>(people);
    }
}