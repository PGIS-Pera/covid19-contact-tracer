package lk.covid19.contact_tracer.asset.person_location_interact_time.dao;


import lk.covid19.contact_tracer.asset.person_location_interact_time.entity.PersonLocationInteractTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonLocationInteractTimeDao extends JpaRepository< PersonLocationInteractTime, Integer > {
}
