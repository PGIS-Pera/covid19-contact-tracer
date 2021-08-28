package lk.covid19.contact_tracer.asset.location_interact.service;


import lk.covid19.contact_tracer.asset.location_interact.entity.LocationInteract;

import java.util.List;

public interface LocationInteractService {

  List< LocationInteract > findAll();

  LocationInteract findById(Integer id);

  LocationInteract persist(LocationInteract district);

  boolean delete(Integer id);

  List< LocationInteract > search(LocationInteract district);
}
