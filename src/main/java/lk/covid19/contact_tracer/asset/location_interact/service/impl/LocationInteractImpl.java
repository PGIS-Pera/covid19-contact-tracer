package lk.covid19.contact_tracer.asset.location_interact.service.impl;


import lk.covid19.contact_tracer.asset.location_interact.dao.LocationInteractDao;
import lk.covid19.contact_tracer.asset.location_interact.entity.LocationInteract;
import lk.covid19.contact_tracer.asset.location_interact.service.LocationInteractService;
import lk.covid19.contact_tracer.util.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@CacheConfig( cacheNames = "visitedPlace" )
@RequiredArgsConstructor
public class LocationInteractImpl implements LocationInteractService {
  private final LocationInteractDao locationInteractDao;
  private final CommonService commonService;

  @Cacheable
  public List< LocationInteract > findAll() {
    return locationInteractDao.findAll();
  }

  @Cacheable
  public LocationInteract findById(Integer id) {
    return locationInteractDao.getById(id);
  }

  @Caching( evict = {@CacheEvict( value = "district", allEntries = true )},
      put = {@CachePut( value = "district", key = "#district.id" )} )
  public LocationInteract persist(LocationInteract district) {
    district.setName(commonService.stringCapitalize(district.getName()));
    return locationInteractDao.save(district);
  }

  @CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    locationInteractDao.deleteById(id);
    return false;
  }

  @Cacheable
  public List< LocationInteract > search(LocationInteract district) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< LocationInteract > districtExample = Example.of(district, matcher);
    return locationInteractDao.findAll(districtExample);
  }

  @Cacheable
  public Page< LocationInteract > findAllPageable(Pageable pageable) {
    return locationInteractDao.findAll(pageable);
  }

}

