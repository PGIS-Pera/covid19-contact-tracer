package lk.covid19.contact_tracer.asset.turn_in_history.dao;


import lk.covid19.contact_tracer.asset.district.entity.District;
import lk.covid19.contact_tracer.asset.turn_in_history.entity.TurnInHistoryNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurnInHistoryNoteDao extends JpaRepository< TurnInHistoryNote, Integer > {

}
