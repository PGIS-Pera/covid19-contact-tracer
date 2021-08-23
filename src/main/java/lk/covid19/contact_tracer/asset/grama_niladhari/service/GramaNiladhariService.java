package lk.covid19.contact_tracer.asset.grama_niladhari.service;


import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface GramaNiladhariService {
  public List< GramaNiladhari > findAll();

  public GramaNiladhari findById(Integer id);

  public GramaNiladhari persist(GramaNiladhari policeStation);

  public boolean delete(Integer id);

  public List< GramaNiladhari > search(GramaNiladhari policeStation);

  public boolean isPoliceStationPresent(GramaNiladhari policeStation);

  Page< GramaNiladhari > findAllPageable(Pageable pageable);

}
