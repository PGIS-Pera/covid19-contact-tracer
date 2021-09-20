package lk.covid19.contact_tracer.asset.person_location_interact_time.service.impl;

import lk.covid19.contact_tracer.asset.common_asset.model.TwoDateGramaNiladhari;
import lk.covid19.contact_tracer.asset.common_asset.model.enums.StopActive;
import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.grama_niladhari.service.GramaNiladhariService;
import lk.covid19.contact_tracer.asset.location_interact.entity.LocationInteract;
import lk.covid19.contact_tracer.asset.location_interact.service.LocationInteractService;
import lk.covid19.contact_tracer.asset.person_location_interact_time.dao.PersonLocationInteractTimeDao;
import lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime;
import lk.covid19.contact_tracer.asset.person_location_interact_time.service.PersonLocationInteractTimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@CacheConfig( cacheNames = "personLocationInteractTime" )
@RequiredArgsConstructor
public class PersonLocationInteractTimeServiceImpl implements PersonLocationInteractTimeService {
  private final PersonLocationInteractTimeDao personLocationInteractTimeDao;
  private final LocationInteractService locationInteractService;
  private final GramaNiladhariService gramaNiladhariService;

  @Cacheable
  public List< PersonLocationInteractTime > findAll() {
    return personLocationInteractTimeDao.findAll();
  }

  @Cacheable
  public PersonLocationInteractTime findById(Integer id) {
    return personLocationInteractTimeDao.getById(id);
  }

  @Caching( evict = {@CacheEvict( value = "personLocationInteractTime", allEntries = true )},
      put = {@CachePut( value = "personLocationInteractTime", key = "#personLocationInteractTime.id" )} )
  public PersonLocationInteractTime persist(PersonLocationInteractTime personLocationInteractTime) {
    // personLocationInteractTime.setName(commonService.stringCapitalize(personLocationInteractTime.getName()));
    return personLocationInteractTimeDao.save(personLocationInteractTime);
  }

  @Caching( evict = {@CacheEvict( value = "personLocationInteractTime", allEntries = true )},
      put = {@CachePut( value = "personLocationInteractTime", key = "'#personLocationInteractTime.id'" )} )
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
  public Map< LocationInteract, List< PersonLocationInteractTime > > searchWithDateTime(TwoDateGramaNiladhari twoDateGramaNiladhari) {
    GramaNiladhari gramaNiladhari = gramaNiladhariService.findById(twoDateGramaNiladhari.getGramaNiladhari().getId());
    List< LocationInteract > locationInteracts = locationInteractService.findByGramaNiladhari(gramaNiladhari);

    Map< LocationInteract, List< PersonLocationInteractTime > > acceptedReport = new HashMap<>();

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
        acceptedReport.put(locationInteract,
                           personLocationInteractTimeDb.stream().sorted(Comparator.comparing(PersonLocationInteractTime::getArrivalAt))
                               .collect(Collectors.toList()));
      }
    }

    return acceptedReport;
  }

}

