package lk.covid19.contact_tracer.asset.grama_niladhari.dao;


import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GramaNiladhariDao extends JpaRepository< GramaNiladhari, Integer > {
}
