package lk.covid19.contact_tracer.asset.turn.service.impl;

import lk.covid19.contact_tracer.asset.turn.dao.TurnDao;
import lk.covid19.contact_tracer.asset.turn.entity.Turn;
import lk.covid19.contact_tracer.asset.turn.service.TurnService;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@CacheConfig( cacheNames = "turn" )
@RequiredArgsConstructor
public class TurnServiceImpl implements TurnService {
  private final TurnDao turnDao;

  @Cacheable
  public List< Turn > findAll() {
    return turnDao.findAll();
  }

  @Cacheable
  public Turn findById(Integer id) {
    return turnDao.getById(id);
  }

  @Caching( evict = {@CacheEvict( value = "turn", allEntries = true )},
      put = {@CachePut( value = "turn", key = "#turn.id" )} )
  public Turn persist(Turn turn) {
    return turnDao.save(turn);
  }

  @CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    turnDao.deleteById(id);
    return false;
  }

  @Cacheable
  public List< Turn > search(Turn turn) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< Turn > turnExample = Example.of(turn, matcher);
    return turnDao.findAll(turnExample);
  }

  @Cacheable
  public Turn findByPatientAndIdentifiedDate(Person person, LocalDate identifiedDate) {
    return turnDao.findByPersonAndIdentifiedDate(person, identifiedDate);
  }

  @Cacheable
  public List< Turn > findByIdentifiedDateIsBetween(LocalDate startDate, LocalDate endDate) {
    return turnDao.findByIdentifiedDateIsBetween(startDate, endDate);
  }

}
