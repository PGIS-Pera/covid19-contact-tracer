package lk.covid19.contact_tracer.asset.person_location_interact_time.service.impl;

import lk.covid19.contact_tracer.asset.person_location_interact_time.dao.PersonLocationInteractTimeDao;
import lk.covid19.contact_tracer.asset.person_location_interact_time.service.PersonLocationInteractTime;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@CacheConfig( cacheNames = "patientVisitedPlaceTime" )
@RequiredArgsConstructor
public class PersonLocationInteractTimeImpl implements PersonLocationInteractTime {
  private final PersonLocationInteractTimeDao personLocationInteractTimeDao;

  @Cacheable
  public List< lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime > findAll() {
    return personLocationInteractTimeDao.findAll();
  }

  @Cacheable
  public lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime findById(Integer id) {
    return personLocationInteractTimeDao.getById(id);
  }

  @Caching( evict = {@CacheEvict( value = "patientVisitedPlaceTime", allEntries = true )},
      put = {@CachePut( value = "patientVisitedPlaceTime", key = "#personLocationInteractTime.id" )} )
  public lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime persist(lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime personLocationInteractTime) {
    // patientVisitedPlaceTime.setName(commonService.stringCapitalize(patientVisitedPlaceTime.getName()));
    return personLocationInteractTimeDao.save(personLocationInteractTime);
  }

  @CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    personLocationInteractTimeDao.deleteById(id);
    return false;
  }

  @Cacheable
  public List< lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime > search(lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime personLocationInteractTime) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime > districtExample = Example.of(personLocationInteractTime, matcher);
    return personLocationInteractTimeDao.findAll(districtExample);
  }


}

