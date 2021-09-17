package lk.covid19.contact_tracer.asset.user_treatment_center.dao;


import lk.covid19.contact_tracer.asset.turn_history.entity.TurnHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTreatmentCenterDao extends JpaRepository< TurnHistory, Integer > {

}
