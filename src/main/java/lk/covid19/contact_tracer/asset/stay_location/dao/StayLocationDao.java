package lk.covid19.contact_tracer.asset.stay_location.dao;


import lk.covid19.contact_tracer.asset.stay_location.entity.StayLocation;
import lk.covid19.contact_tracer.asset.turn_history.entity.TurnHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StayLocationDao extends JpaRepository< StayLocation, Integer > {

}
