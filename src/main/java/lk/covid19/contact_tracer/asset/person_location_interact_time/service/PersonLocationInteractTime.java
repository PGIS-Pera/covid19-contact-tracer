package lk.covid19.contact_tracer.asset.person_location_interact_time.service;


import java.util.List;

public interface PersonLocationInteractTime {

  List< lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime > findAll();

  lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime findById(Integer id);

  lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime persist(lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime district);

  boolean delete(Integer id);

  List< lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime > search(lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime district);

}
