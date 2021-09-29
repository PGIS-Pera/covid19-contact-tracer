package lk.covid19.contact_tracer.asset.location_interact.service;


import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.location_interact.entity.LocationInteract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocationInteractService {

  List< LocationInteract > findAll();

  LocationInteract findById(Integer id);

  LocationInteract persist(LocationInteract district);

  boolean delete(Integer id);

  List< LocationInteract > search(LocationInteract district);

  Page< LocationInteract > findAllPageable(Pageable pageable);

  LocationInteract findByGramaNiladhariAndName(LocationInteract locationInteract);

  List< LocationInteract > findByGramaNiladhari(GramaNiladhari gramaNiladhari);
}
