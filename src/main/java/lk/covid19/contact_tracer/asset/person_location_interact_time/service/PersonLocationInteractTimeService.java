package lk.covid19.contact_tracer.asset.person_location_interact_time.service;


import lk.covid19.contact_tracer.asset.common_asset.model.LocationInteractTimeReport;
import lk.covid19.contact_tracer.asset.common_asset.model.TwoDateGramaNiladhari;
import lk.covid19.contact_tracer.asset.location_interact.entity.LocationInteract;
import lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PersonLocationInteractTimeService {

  PersonLocationInteractTime findLastOne();

  List< PersonLocationInteractTime > findAll();

  PersonLocationInteractTime findById(Integer id);

  PersonLocationInteractTime persist(PersonLocationInteractTime personLocationInteractTime);

  List< PersonLocationInteractTime > persistAll(List< PersonLocationInteractTime > personLocationInteractTimes);

  boolean delete(Integer id);

  List< PersonLocationInteractTime > search(PersonLocationInteractTime personLocationInteractTime);

  List< LocationInteractTimeReport > searchWithDateTime(TwoDateGramaNiladhari twoDateGramaNiladhari);

  List< LocationInteractTimeReport > findByArrivalAtBetween(LocalDate identifiedDate);

}
