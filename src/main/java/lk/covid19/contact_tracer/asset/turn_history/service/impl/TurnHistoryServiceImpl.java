package lk.covid19.contact_tracer.asset.turn_history.service.impl;


import lk.covid19.contact_tracer.asset.turn_history.dao.TurnHistoryDao;
import lk.covid19.contact_tracer.asset.turn_history.entity.TurnHistory;
import lk.covid19.contact_tracer.asset.turn_history.service.TurnHistoryService;
import lk.covid19.contact_tracer.asset.turn_history.service.TurnHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig( cacheNames = "turnHistory" )
@RequiredArgsConstructor
public class TurnHistoryServiceImpl implements TurnHistoryService {
  private final TurnHistoryDao turnHistoryDao;


  @Cacheable
  public List< TurnHistory > findAll() {
    return turnHistoryDao.findAll();
  }

  @Cacheable
  public TurnHistory findById(Integer id) {
    return turnHistoryDao.getById(id);
  }

  @Caching( evict = {@CacheEvict( value = "turnHistory", allEntries = true )},
      put = {@CachePut( value = "turnHistory", key = "#turnHistory.id" )} )
  public TurnHistory persist(TurnHistory turnHistory) {
    return turnHistoryDao.save(turnHistory);
  }

  @CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    turnHistoryDao.deleteById(id);
    return false;
  }

  @Cacheable
  public List< TurnHistory > search(TurnHistory turnHistory) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< TurnHistory > turnHistoryExample = Example.of(turnHistory, matcher);
    return turnHistoryDao.findAll(turnHistoryExample);
  }

}
