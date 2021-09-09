package lk.covid19.contact_tracer.asset.turn_in_history.service;


import lk.covid19.contact_tracer.asset.district.entity.District;
import lk.covid19.contact_tracer.asset.turn_in_history.entity.TurnInHistoryNote;

import java.util.List;


public interface TurnInHistoryNoteService {

  List< TurnInHistoryNote > findAll();

  TurnInHistoryNote findById(Integer id);

  TurnInHistoryNote persist(TurnInHistoryNote turnInHistoryNote);

  boolean delete(Integer id);

  List< TurnInHistoryNote > search(TurnInHistoryNote turnInHistoryNote);

}
