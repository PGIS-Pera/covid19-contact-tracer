package lk.covid19.contact_tracer.asset.user_treatment_center.service;


import lk.covid19.contact_tracer.asset.turn_history.entity.TurnHistory;

import java.util.List;


public interface UserTreatmentCenterService {

  List< TurnHistory > findAll();

  TurnHistory findById(Integer id);

  TurnHistory persist(TurnHistory turnInHistoryNote);

  boolean delete(Integer id);

  List< TurnHistory > search(TurnHistory turnInHistoryNote);

}
