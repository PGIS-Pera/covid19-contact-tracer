package lk.covid19.contact_tracer.asset.person_location_interact_time.service.impl;

import lk.covid19.contact_tracer.asset.common_asset.model.LocationInteractTimeReport;
import lk.covid19.contact_tracer.asset.common_asset.model.TwoDateGramaNiladhari;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.StopActive;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.grama_niladhari.service.GramaNiladhariService;
import lk.covid19.contact_tracer.asset.location_interact.entity.LocationInteract;
import lk.covid19.contact_tracer.asset.location_interact.service.LocationInteractService;
import lk.covid19.contact_tracer.asset.person_location_interact_time.dao.PersonLocationInteractTimeDao;
import lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime;
import lk.covid19.contact_tracer.asset.person_location_interact_time.service.PersonLocationInteractTimeService;
import lk.covid19.contact_tracer.asset.turn.entity.Turn;
import lk.covid19.contact_tracer.asset.turn.service.TurnService;
import lk.covid19.contact_tracer.util.service.DateTimeAgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@CacheConfig( cacheNames = "personLocationInteractTime" )
@RequiredArgsConstructor
public class PersonLocationInteractTimeServiceImpl implements PersonLocationInteractTimeService {
  private final PersonLocationInteractTimeDao personLocationInteractTimeDao;
  private final LocationInteractService locationInteractService;
  private final GramaNiladhariService gramaNiladhariService;
  private final DateTimeAgeService dateTimeAgeService;

  @Cacheable
  public PersonLocationInteractTime findLastOne() {
    return personLocationInteractTimeDao.findFirstByOrderByIdDesc();
  }

  @Cacheable
  public List< PersonLocationInteractTime > findAll() {
    return personLocationInteractTimeDao.findAll();
  }

  @Cacheable
  public PersonLocationInteractTime findById(Integer id) {
    return personLocationInteractTimeDao.getById(id);
  }

  @Caching( evict = {@CacheEvict( value = "personLocationInteractTime", allEntries = true )}, put =
      {@CachePut( value = "personLocationInteractTime", key = "#personLocationInteractTime.id" )} )
  public PersonLocationInteractTime persist(PersonLocationInteractTime personLocationInteractTime) {
    // personLocationInteractTime.setName(commonService.stringCapitalize(personLocationInteractTime.getName()));
    return personLocationInteractTimeDao.save(personLocationInteractTime);
  }

  @Caching( evict = {@CacheEvict( value = "personLocationInteractTime", allEntries = true )}, put =
      {@CachePut( value = "personLocationInteractTime", key = "'#personLocationInteractTime.id'" )} )
  public List< PersonLocationInteractTime > persistAll(List< PersonLocationInteractTime > personLocationInteractTimes) {
    // personLocationInteractTime.setName(commonService.stringCapitalize(personLocationInteractTime.getName()));
    return personLocationInteractTimeDao.saveAll(personLocationInteractTimes);
  }

  @CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    personLocationInteractTimeDao.deleteById(id);
    return false;
  }

  @Cacheable
  public List< PersonLocationInteractTime > search(PersonLocationInteractTime personLocationInteractTime) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< PersonLocationInteractTime > districtExample = Example.of(personLocationInteractTime, matcher);
    return personLocationInteractTimeDao.findAll(districtExample);
  }

  @Cacheable
  public List< LocationInteractTimeReport > searchWithDateTime(TwoDateGramaNiladhari twoDateGramaNiladhari) {
    GramaNiladhari gramaNiladhari = gramaNiladhariService.findById(twoDateGramaNiladhari.getGramaNiladhari().getId());
    List< LocationInteract > locationInteracts = locationInteractService.findByGramaNiladhari(gramaNiladhari);

    List< LocationInteractTimeReport > acceptedReport = new ArrayList<>();

    for ( LocationInteract locationInteract : locationInteracts ) {
      List< PersonLocationInteractTime > personLocationInteractTimeDb =
          personLocationInteractTimeDao
              .findByLocationInteractAndArrivalAtBetweenAndLeaveAtBetweenAndStopActive(
                  locationInteract,
                  twoDateGramaNiladhari.getArrivalAt(),
                  twoDateGramaNiladhari.getLeaveAt(),
                  twoDateGramaNiladhari.getArrivalAt(),
                  twoDateGramaNiladhari.getLeaveAt(),
                  StopActive.ACTIVE);

      if ( !personLocationInteractTimeDb.isEmpty() ) {
        LocationInteractTimeReport report = LocationInteractTimeReport.builder()
            .locationInteract(locationInteract)
            .personLocationInteractTimes(personLocationInteractTimeDb
                                             .stream()
                                             .sorted(Comparator.comparing(PersonLocationInteractTime::getArrivalAt))
                                             .collect(Collectors.toList()))
            .build();
        acceptedReport.add(report);
      }
    }

    return acceptedReport;
  }

  @Cacheable
  public List< LocationInteractTimeReport > findByArrivalAtBetween(LocalDate identifiedDate) {
    return personLocationInteractTimeDao.findByArrivalAtBetween(dateTimeAgeService.dateTimeToLocalDateStartInDay(identifiedDate), LocalDateTime.now());
  }

  @Cacheable
  public List< PersonLocationInteractTime > findByTurn(Turn turn) {
    return personLocationInteractTimeDao.findByTurn(turn);
  }


}

