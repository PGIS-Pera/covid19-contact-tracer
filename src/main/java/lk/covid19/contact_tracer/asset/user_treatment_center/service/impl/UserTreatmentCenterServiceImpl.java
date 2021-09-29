package lk.covid19.contact_tracer.asset.user_treatment_center.service.impl;


import lk.covid19.contact_tracer.asset.turn_history.entity.TurnHistory;
import lk.covid19.contact_tracer.asset.user_treatment_center.dao.UserTreatmentCenterDao;
import lk.covid19.contact_tracer.asset.user_treatment_center.service.UserTreatmentCenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig( cacheNames = "turnHistory" )
@RequiredArgsConstructor
public class UserTreatmentCenterServiceImpl implements UserTreatmentCenterService {
  private final UserTreatmentCenterDao userTreatmentCenterDao;


  @Cacheable
  public List< TurnHistory > findAll() {
    return userTreatmentCenterDao.findAll();
  }

  @Cacheable
  public TurnHistory findById(Integer id) {
    return userTreatmentCenterDao.getById(id);
  }

  @Caching( evict = {@CacheEvict( value = "turnHistory", allEntries = true )},
      put = {@CachePut( value = "turnHistory", key = "#turnHistory.id" )} )
  public TurnHistory persist(TurnHistory turnHistory) {
    return userTreatmentCenterDao.save(turnHistory);
  }

  @CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    userTreatmentCenterDao.deleteById(id);
    return false;
  }

  @Cacheable
  public List< TurnHistory > search(TurnHistory turnHistory) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< TurnHistory > turnHistoryExample = Example.of(turnHistory, matcher);
    return userTreatmentCenterDao.findAll(turnHistoryExample);
  }

}
