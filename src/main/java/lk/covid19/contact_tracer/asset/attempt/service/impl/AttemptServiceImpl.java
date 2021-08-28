package lk.covid19.contact_tracer.asset.attempt.service.impl;

import lk.covid19.contact_tracer.asset.attempt.dao.AttemptDao;
import lk.covid19.contact_tracer.asset.attempt.entity.Attempt;
import lk.covid19.contact_tracer.asset.attempt.service.AttemptService;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@CacheConfig( cacheNames = "attempt" )
@RequiredArgsConstructor
public class AttemptServiceImpl implements AttemptService {
  private final AttemptDao attemptDao;

  @Cacheable
  public List< Attempt > findAll() {
    return attemptDao.findAll();
  }

  @Cacheable
  public Attempt findById(Integer id) {
    return attemptDao.getById(id);
  }

  @Caching( evict = {@CacheEvict( value = "attempt", allEntries = true )},
      put = {@CachePut( value = "attempt", key = "#attempt.id" )} )
  public Attempt persist(Attempt attempt) {
    return attemptDao.save(attempt);
  }

  @CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    attemptDao.deleteById(id);
    return false;
  }

  @Cacheable
  public List< Attempt > search(Attempt attempt) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< Attempt > attemptExample = Example.of(attempt, matcher);
    return attemptDao.findAll(attemptExample);
  }

  @Cacheable
  public Attempt findByPatientAndIdentifiedDate(Person person, LocalDate identifiedDate) {
    return attemptDao.findByPatientAndIdentifiedDate(person, identifiedDate);
  }

  @Cacheable
  public List< Attempt > findByIdentifiedDateIsBetween(LocalDate startDate, LocalDate endDate) {
    return attemptDao.findByIdentifiedDateIsBetween(startDate, endDate);
  }

}
