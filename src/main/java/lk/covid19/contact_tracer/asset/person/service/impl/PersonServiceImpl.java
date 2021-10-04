package lk.covid19.contact_tracer.asset.person.service.impl;


import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.person_location_interact_time.service.PersonLocationInteractTimeService;
import lk.covid19.contact_tracer.asset.turn.entity.Turn;
import lk.covid19.contact_tracer.asset.turn.service.TurnService;
import lk.covid19.contact_tracer.asset.person.dao.PersonDao;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import lk.covid19.contact_tracer.asset.person.entity.enums.PersonStatus;
import lk.covid19.contact_tracer.asset.person.service.PersonService;
import lk.covid19.contact_tracer.util.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig( cacheNames = "person" )
public class PersonServiceImpl implements PersonService {

  private final PersonDao personDao;
  private final CommonService commonService;
  private final TurnService turnService;
  private final PersonLocationInteractTimeService personLocationInteractTimeService;

  @Cacheable
  public Page< Person > findAllPageable(Pageable pageable) {
    return personDao.findAll(pageable);
  }

  @Cacheable
  public List< Person > findAll() {
    return personDao.findAll(Sort.by(Sort.Direction.DESC, "id"));
  }

  @Cacheable
  public Person findById(Integer id) {
    return personDao.getById(id);
  }

  @Caching( evict = {@CacheEvict( value = "person", allEntries = true )},
      put = {@CachePut( value = "person", key = "#person.id" )} )
  public Person persist(Person person) {
    if ( person.getId() == null ) {
      personPersist(person);
    }
    person.setMobile(person.getMobile() != null ?
                         commonService.phoneNumberLengthValidator(person.getMobile()) : null);
    person.setName(commonService.stringCapitalize(person.getName()));
    return personDao.save(person);
  }

  @Caching( evict = {@CacheEvict( value = "person", allEntries = true )},
      put = {@CachePut( value = "person", key = "'#person.id'" )} )
  public List< Person > persistList(List< Person > person) {
    return personDao.saveAll(person);
  }

  private void personPersist(Person person) {
    person.setPersonStatus(PersonStatus.GENERAL);
    if ( personDao.findFirstByOrderByIdDesc() == null ) {
      person.setCode("LKCP" + commonService.numberAutoGen(null).toString());
    } else {
      String previousCode = personDao.findFirstByOrderByIdDesc().getCode().substring(4);
      person.setCode("LKCP" + commonService.numberAutoGen(previousCode).toString());
    }
  }

  public boolean delete(Integer id) {
    //this was prohibited method
    return false;
  }

  @Cacheable
  public List< Person > search(Person person) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< Person > personExample = Example.of(person, matcher);
    return personDao.findAll(personExample);
  }

  @Cacheable
  public Person findByNic(String nic) {
    Person person = personDao.findByNic(nic);
    if ( person == null ) {
      person = new Person();
    }
    return person;
  }

  @Cacheable
  public Person findLastPatient() {
    return personDao.findFirstByOrderByIdDesc();
  }

  @Cacheable
  public List< Person > findByTurnIdentifiedDateRange(LocalDate startDate, LocalDate endDate) {
    HashSet< Person > person = new HashSet<>();
    for ( Turn turn : turnService.findByIdentifiedDateIsBetween(startDate, endDate) ) {
      person.add(personDao.getById(turn.getPerson().getId()));
    }
    return new ArrayList<>(person);
  }

  @Cacheable
  public void saveAndTurn(Person person) {
    Person personDb = personDao.getById(person.getId());
    personDb.setPersonStatus(PersonStatus.INFECTED);
    personDao.save(personDb);
    person.getTurns().forEach(turnService::persist);
    personLocationInteractTimeService.persistAll(person.getPersonLocationInteractTimes());
  }

  @Cacheable
  public List< Person > findByGramaNiladhari(GramaNiladhari gramaNiladhari) {
    return personDao.findByGramaNiladhari(gramaNiladhari);
  }

}
