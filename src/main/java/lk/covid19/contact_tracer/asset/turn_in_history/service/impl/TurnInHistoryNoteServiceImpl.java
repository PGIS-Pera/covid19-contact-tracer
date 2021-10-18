package lk.covid19.contact_tracer.asset.turn_in_history.service.impl;


import lk.covid19.contact_tracer.asset.district.entity.District;
import lk.covid19.contact_tracer.asset.turn_in_history.dao.TurnInHistoryNoteDao;
import lk.covid19.contact_tracer.asset.turn_in_history.entity.TurnInHistoryNote;
import lk.covid19.contact_tracer.asset.turn_in_history.service.TurnInHistoryNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig( cacheNames = "turnInHistoryNote" )
@RequiredArgsConstructor
public class TurnInHistoryNoteServiceImpl implements TurnInHistoryNoteService {
  private final TurnInHistoryNoteDao turnInHistoryNoteDao;


  @Cacheable
  public List< TurnInHistoryNote > findAll() {
    return turnInHistoryNoteDao.findAll();
  }

  @Cacheable
  public TurnInHistoryNote findById(Integer id) {
    return turnInHistoryNoteDao.getById(id);
  }

  @Caching( evict = {@CacheEvict( value = "turnInHistoryNote", allEntries = true )}, put = {@CachePut( value =
      "turnInHistoryNote", key = "#turnInHistoryNote.id" )} )
  public TurnInHistoryNote persist(TurnInHistoryNote turnInHistoryNote) {
    return turnInHistoryNoteDao.save(turnInHistoryNote);
  }

  @CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    turnInHistoryNoteDao.deleteById(id);
    return false;
  }

  @Cacheable
  public List< TurnInHistoryNote > search(TurnInHistoryNote turnInHistoryNote) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< TurnInHistoryNote > turnInHistoryNoteExample = Example.of(turnInHistoryNote, matcher);
    return turnInHistoryNoteDao.findAll(turnInHistoryNoteExample);
  }

}
