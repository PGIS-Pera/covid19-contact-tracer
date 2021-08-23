package lk.covid19.contact_tracer.asset.patient_visited_place_time.service.impl;

import lk.covid19.contact_tracer.asset.patient_visited_place_time.dao.PatientVisitedPlaceTimeDao;
import lk.covid19.contact_tracer.asset.patient_visited_place_time.entity.PatientVisitedPlaceTime;
import lk.covid19.contact_tracer.asset.patient_visited_place_time.service.PatientVisitedPlaceTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@CacheConfig( cacheNames = "patientVisitedPlaceTime" )
@RequiredArgsConstructor
public class PatientVisitedPlaceTimeImpl implements PatientVisitedPlaceTimeService {
  private final PatientVisitedPlaceTimeDao patientVisitedPlaceTimeDao;

  @Cacheable
  public List< PatientVisitedPlaceTime > findAll() {
    return patientVisitedPlaceTimeDao.findAll();
  }

  @Cacheable
  public PatientVisitedPlaceTime findById(Integer id) {
    return patientVisitedPlaceTimeDao.getById(id);
  }

  @Caching( evict = {@CacheEvict( value = "patientVisitedPlaceTime", allEntries = true )},
      put = {@CachePut( value = "patientVisitedPlaceTime", key = "#patientVisitedPlaceTime.id" )} )
  public PatientVisitedPlaceTime persist(PatientVisitedPlaceTime patientVisitedPlaceTime) {
    // patientVisitedPlaceTime.setName(commonService.stringCapitalize(patientVisitedPlaceTime.getName()));
    return patientVisitedPlaceTimeDao.save(patientVisitedPlaceTime);
  }

  @CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    patientVisitedPlaceTimeDao.deleteById(id);
    return false;
  }

  @Cacheable
  public List< PatientVisitedPlaceTime > search(PatientVisitedPlaceTime patientVisitedPlaceTime) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< PatientVisitedPlaceTime > districtExample = Example.of(patientVisitedPlaceTime, matcher);
    return patientVisitedPlaceTimeDao.findAll(districtExample);
  }


}

