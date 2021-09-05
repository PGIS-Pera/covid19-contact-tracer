package lk.covid19.contact_tracer.asset.person_location_interact_time.service;


import lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime;

import java.util.List;

public interface PersonLocationInteractTimeService {

  List< PersonLocationInteractTime > findAll();

  PersonLocationInteractTime findById(Integer id);

  PersonLocationInteractTime persist(PersonLocationInteractTime personLocationInteractTime);

  List< PersonLocationInteractTime > persistAll(List< PersonLocationInteractTime > personLocationInteractTimes);

  boolean delete(Integer id);

  List< PersonLocationInteractTime > search(PersonLocationInteractTime personLocationInteractTime);

}
