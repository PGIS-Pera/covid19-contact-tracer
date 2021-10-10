package lk.covid19.contact_tracer.asset.location_interact.service.impl;


import lk.covid19.contact_tracer.asset.grama_niladhari.dao.GramaNiladhariDao;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.grama_niladhari.service.GramaNiladhariService;
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
@CacheConfig( cacheNames = "locationInteract" )
@RequiredArgsConstructor
public class LocationInteractServiceImpl implements LocationInteractService {
  private final LocationInteractDao locationInteractDao;
  private final CommonService commonService;
  private final GramaNiladhariService gramaNiladhariService;

  @Cacheable
  public List< LocationInteract > findAll() {
    return locationInteractDao.findAll();
  }

  @Cacheable
  public LocationInteract findById(Integer id) {
    return locationInteractDao.getById(id);
  }

  @Caching( evict = {@CacheEvict( value = "locationInteract", allEntries = true )},
      put = {@CachePut( value = "locationInteract", key = "#locationInteract.id" )} )
  public LocationInteract persist(LocationInteract locationInteract) {
    locationInteract.setName(commonService.stringCapitalize(locationInteract.getName()));
    return locationInteractDao.save(locationInteract);
  }

  @CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    locationInteractDao.deleteById(id);
    return false;
  }

  @Cacheable
  public List< LocationInteract > search(LocationInteract locationInteract) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< LocationInteract > locationInteractExample = Example.of(locationInteract, matcher);
    return locationInteractDao.findAll(locationInteractExample);
  }

  @Cacheable
  public Page< LocationInteract > findAllPageable(Pageable pageable) {
    return locationInteractDao.findAll(pageable);
  }

  @Cacheable
  public LocationInteract findByGramaNiladhariAndName(LocationInteract locationInteract) {
    return locationInteractDao.findByNameAndGramaNiladhari(commonService.stringCapitalize(locationInteract.getName()),
                                                           locationInteract.getGramaNiladhari());
  }

  public List< LocationInteract > findByGramaNiladhari(GramaNiladhari gramaNiladhari) {
    return locationInteractDao.findByGramaNiladhari(gramaNiladhari);
  }

}

