package lk.covid19.contact_tracer.asset.turn_history.service;


import lk.covid19.contact_tracer.asset.turn_history.entity.TurnHistory;

import java.util.List;


public interface TurnHistoryService {

  List< TurnHistory > findAll();

  TurnHistory findById(Integer id);

  TurnHistory persist(TurnHistory turnInHistoryNote);

  boolean delete(Integer id);

  List< TurnHistory > search(TurnHistory turnInHistoryNote);

}
