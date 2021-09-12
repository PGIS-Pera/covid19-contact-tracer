package lk.covid19.contact_tracer.asset.location_interact.dao;


import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.location_interact.entity.LocationInteract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface LocationInteractDao extends JpaRepository< LocationInteract, Integer > {
  LocationInteract findByNameAndGramaNiladhari(String name, GramaNiladhari gramaNiladhari);
}
