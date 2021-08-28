package lk.covid19.contact_tracer.asset.location_interact.dao;


import lk.covid19.contact_tracer.asset.location_interact.entity.LocationInteract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationInteractDao extends JpaRepository< LocationInteract, Integer > {

}
