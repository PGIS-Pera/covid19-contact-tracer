package lk.covid19.contact_tracer.asset.person_location_interact_time.service.impl;

import lk.covid19.contact_tracer.asset.person_location_interact_time.dao.PersonLocationInteractTimeDao;
import lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime;
import lk.covid19.contact_tracer.asset.person_location_interact_time.service.PersonLocationInteractTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@CacheConfig( cacheNames = "personLocationInteractTime" )
@RequiredArgsConstructor
public class PersonLocationInteractTimeServiceImpl implements PersonLocationInteractTimeService {
  private final PersonLocationInteractTimeDao personLocationInteractTimeDao;

  @Cacheable
  public List< PersonLocationInteractTime > findAll() {
    return personLocationInteractTimeDao.findAll();
  }

  @Cacheable
  public PersonLocationInteractTime findById(Integer id) {
    return personLocationInteractTimeDao.getById(id);
  }

  @Caching( evict = {@CacheEvict( value = "personLocationInteractTime", allEntries = true )},
      put = {@CachePut( value = "personLocationInteractTime", key = "#personLocationInteractTime.id" )} )
  public PersonLocationInteractTime persist(PersonLocationInteractTime personLocationInteractTime) {
    // personLocationInteractTime.setName(commonService.stringCapitalize(personLocationInteractTime.getName()));
    return personLocationInteractTimeDao.save(personLocationInteractTime);
  }

  @Caching( evict = {@CacheEvict( value = "personLocationInteractTime", allEntries = true )},
      put = {@CachePut( value = "personLocationInteractTime", key = "'#personLocationInteractTime.id'" )} )
  public List< PersonLocationInteractTime > persistAll(List< PersonLocationInteractTime > personLocationInteractTimes) {
    // personLocationInteractTime.setName(commonService.stringCapitalize(personLocationInteractTime.getName()));
    return personLocationInteractTimeDao.saveAll(personLocationInteractTimes);
  }

  @CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    personLocationInteractTimeDao.deleteById(id);
    return false;
  }

  @Cacheable
  public List< PersonLocationInteractTime > search(PersonLocationInteractTime personLocationInteractTime) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< PersonLocationInteractTime > districtExample = Example.of(personLocationInteractTime, matcher);
    return personLocationInteractTimeDao.findAll(districtExample);
  }


}

