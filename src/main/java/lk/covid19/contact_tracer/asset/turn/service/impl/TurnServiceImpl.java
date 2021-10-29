package lk.covid19.contact_tracer.asset.turn.service.impl;

import lk.covid19.contact_tracer.asset.person.entity.enums.PersonStatus;
import lk.covid19.contact_tracer.asset.person.service.PersonService;
import lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime;
import lk.covid19.contact_tracer.asset.person_location_interact_time.service.PersonLocationInteractTimeService;
import lk.covid19.contact_tracer.asset.turn.dao.TurnDao;
import lk.covid19.contact_tracer.asset.turn.entity.Turn;
import lk.covid19.contact_tracer.asset.turn.service.TurnService;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import org.springframework.cache.annotation.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@CacheConfig( cacheNames = "turn" )
public class TurnServiceImpl implements TurnService {
  private final TurnDao turnDao;
  private final PersonService personService;
  private final PersonLocationInteractTimeService personLocationInteractTimeService;

  public TurnServiceImpl(TurnDao turnDao, @Lazy PersonService personService,
                         PersonLocationInteractTimeService personLocationInteractTimeService) {
    this.turnDao = turnDao;
    this.personService = personService;
    this.personLocationInteractTimeService = personLocationInteractTimeService;
  }

  @Cacheable
  public List< Turn > findAll() {
    return turnDao.findAll();
  }

  @Cacheable
  public Turn findById(Integer id) {
    return turnDao.getById(id);
  }

  @Caching( evict = {@CacheEvict( value = "turn", allEntries = true )}, put = {@CachePut( value = "turn", key =
      "#turn.id" )} )
  public Turn persist(Turn turn) {

    if ( turn.getId() != null ) {
      final Turn dbTurn = turnDao.getById(turn.getId());
      if ( !turn.getPersonLocationInteractTimes().isEmpty() ) {
        turn.getPersonLocationInteractTimes().forEach(x -> {
          if ( x.getId() == null ) {
            x.setTurn(dbTurn);
            personLocationInteractTimeService.persist(x);
          } else {
            PersonLocationInteractTime personLocationInteractTime =
                personLocationInteractTimeService.findById(x.getId());
            if ( !personLocationInteractTime.getStopActive().equals(x.getStopActive()) ) {
              personLocationInteractTimeService.persist(x);
            }
          }

        });
      }
      if ( dbTurn.getIdentifiedDate().equals(turn.getIdentifiedDate()) ) {
        turn = dbTurn;
      } else {
        turn = turnDao.save(dbTurn);
      }
    } else {
      turn = turnDao.save(turn);
    }
    return turn;
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

  @Cacheable
  public Page< Turn > findAllPageable(Pageable pageable) {
    return turnDao.findAll(pageable);
  }

  @Cacheable
  public List< Turn > findByPerson(Person person) {
    List< Person > result = personService.search(person);

    List< Turn > turns = new ArrayList<>();
    result.forEach(x -> turns.addAll(turnDao.findByPerson(x)));

    return turns.stream().distinct().collect(Collectors.toList());
  }

  @Cacheable
  public List< Turn > findByPersonStatus(PersonStatus personStatus) {
    return turnDao.findByPersonStatus(personStatus);
  }

  public List< Turn > findByCreatedAtIsBetween(LocalDateTime form, LocalDateTime to) {
    return turnDao.findByCreatedAtIsBetween(form, to);
  }

  @Cacheable
  public List< Turn > findByCreatedAtIsBetweenAndCreatedBy(LocalDateTime form,
                                                           LocalDateTime to,
                                                           String username) {
    return turnDao.findByCreatedAtIsBetweenAndCreatedBy(form, to, username);
  }

  @Cacheable
  public List< Turn > findByCreatedBy(String username) {
    return turnDao.findByCreatedBy(username);
  }

  @Cacheable
  public Turn findLastTurn() {
    return turnDao.findFirstByOrderByIdDesc();
  }


}
