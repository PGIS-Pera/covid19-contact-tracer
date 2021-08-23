package lk.covid19.contact_tracer.asset.visited_place.service;


import lk.covid19.contact_tracer.asset.visited_place.entity.VisitedPlace;

import java.util.List;

public interface VisitedPlaceService {

  List< VisitedPlace > findAll();

  VisitedPlace findById(Integer id);

  VisitedPlace persist(VisitedPlace district);

  boolean delete(Integer id);

  List< VisitedPlace > search(VisitedPlace district);
}
