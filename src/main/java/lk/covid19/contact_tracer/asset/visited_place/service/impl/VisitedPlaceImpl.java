package lk.covid19.contact_tracer.asset.visited_place.service.impl;


import lk.covid19.contact_tracer.asset.visited_place.dao.VisitedPlaceDao;
import lk.covid19.contact_tracer.asset.visited_place.entity.VisitedPlace;
import lk.covid19.contact_tracer.asset.visited_place.service.VisitedPlaceService;
import lk.covid19.contact_tracer.util.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@CacheConfig( cacheNames = "visitedPlace" )
@RequiredArgsConstructor
public class VisitedPlaceImpl implements VisitedPlaceService {
  private final VisitedPlaceDao visitedPlaceDao;
  private final CommonService commonService;

  @Cacheable
  public List< VisitedPlace > findAll() {
    return visitedPlaceDao.findAll();
  }

  @Cacheable
  public VisitedPlace findById(Integer id) {
    return visitedPlaceDao.getById(id);
  }

  @Caching( evict = {@CacheEvict( value = "district", allEntries = true )},
      put = {@CachePut( value = "district", key = "#district.id" )} )
  public VisitedPlace persist(VisitedPlace district) {
    district.setName(commonService.stringCapitalize(district.getName()));
    return visitedPlaceDao.save(district);
  }

  @CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    visitedPlaceDao.deleteById(id);
    return false;
  }

  @Cacheable
  public List< VisitedPlace > search(VisitedPlace district) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< VisitedPlace > districtExample = Example.of(district, matcher);
    return visitedPlaceDao.findAll(districtExample);
  }

}

