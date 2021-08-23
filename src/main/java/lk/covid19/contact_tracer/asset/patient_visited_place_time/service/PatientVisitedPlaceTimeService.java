package lk.covid19.contact_tracer.asset.patient_visited_place_time.service;


import lk.covid19.contact_tracer.asset.patient_visited_place_time.entity.PatientVisitedPlaceTime;

import java.util.List;

public interface PatientVisitedPlaceTimeService {

  List< PatientVisitedPlaceTime > findAll();

  PatientVisitedPlaceTime findById(Integer id);

  PatientVisitedPlaceTime persist(PatientVisitedPlaceTime district);

  boolean delete(Integer id);

  List< PatientVisitedPlaceTime > search(PatientVisitedPlaceTime district);

}
