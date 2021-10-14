package lk.covid19.contact_tracer.asset.stay_location.service.impl;


import lk.covid19.contact_tracer.asset.stay_location.dao.StayLocationDao;
import lk.covid19.contact_tracer.asset.stay_location.service.StayLocationService;
import lk.covid19.contact_tracer.asset.stay_location.entity.StayLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@CacheConfig( cacheNames = "stayLocation" )
@RequiredArgsConstructor
public class StayLocationServiceImpl implements StayLocationService {
  private final StayLocationDao stayLocationDao;


  //@Cacheable
  public List< StayLocation > findAll() {
    return stayLocationDao.findAll();
  }

  //@Cacheable
  public StayLocation findById(Integer id) {
    return stayLocationDao.getById(id);
  }

  //@Caching( evict = {//@CacheEvict( value = "stayLocation", allEntries = true )},      put = {@CachePut( value = "stayLocation", key = "#stayLocation.id" )} )
  public StayLocation persist(StayLocation stayLocation) {
    return stayLocationDao.save(stayLocation);
  }

  //@CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    stayLocationDao.deleteById(id);
    return false;
  }

  //@Cacheable
  public List< StayLocation > search(StayLocation stayLocation) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< StayLocation > stayLocationExample = Example.of(stayLocation, matcher);
    return stayLocationDao.findAll(stayLocationExample);
  }

}
