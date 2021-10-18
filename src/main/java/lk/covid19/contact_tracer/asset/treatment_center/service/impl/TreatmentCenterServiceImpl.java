package lk.covid19.contact_tracer.asset.treatment_center.service.impl;


import lk.covid19.contact_tracer.asset.treatment_center.dao.TreatmentCenterDao;
import lk.covid19.contact_tracer.asset.treatment_center.entity.TreatmentCenter;
import lk.covid19.contact_tracer.asset.treatment_center.service.TreatmentCenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig( cacheNames = "treatmentCenter" )
@RequiredArgsConstructor
public class TreatmentCenterServiceImpl implements TreatmentCenterService {
  private final TreatmentCenterDao treatmentCenterDao;

  @Cacheable
  public List< TreatmentCenter > findAll() {
    return treatmentCenterDao.findAll();
  }

  @Cacheable
  public TreatmentCenter findById(Integer id) {
    return treatmentCenterDao.getById(id);
  }

  @Caching( evict = {@CacheEvict( value = "quarantineCenter", allEntries = true )}, put = {@CachePut( value =
      "quarantineCenter", key = "#treatmentCenter.id" )} )
  public TreatmentCenter persist(TreatmentCenter treatmentCenter) {
    return treatmentCenterDao.save(treatmentCenter);
  }

  @CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    treatmentCenterDao.deleteById(id);
    return false;
  }

  @Cacheable
  public List< TreatmentCenter > search(TreatmentCenter treatmentCenter) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< TreatmentCenter > policeStationExample = Example.of(treatmentCenter, matcher);
    return treatmentCenterDao.findAll(policeStationExample);
  }

  @Cacheable
  public Page< TreatmentCenter > findAllPageable(Pageable pageable) {
    return treatmentCenterDao.findAll(pageable);
  }

}
