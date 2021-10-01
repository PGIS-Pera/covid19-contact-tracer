package lk.covid19.contact_tracer.asset.grama_niladhari.service;


import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface GramaNiladhariService {
   List< GramaNiladhari > findAll();

   GramaNiladhari findById(Integer id);

   GramaNiladhari persist(GramaNiladhari gramaNiladhari);

   boolean delete(Integer id);

   List< GramaNiladhari > search(GramaNiladhari gramaNiladhari);

  Page< GramaNiladhari > findAllPageable(Pageable pageable);
}
