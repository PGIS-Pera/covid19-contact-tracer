package lk.covid19.contact_tracer.asset.person.dao;


import lk.covid19.contact_tracer.asset.grama_niladhari.entity.GramaNiladhari;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonDao extends JpaRepository< Person, Integer > {
  Person findFirstByOrderByIdDesc();

  Person findByNic(String nic);

  List< Person > findByGramaNiladhari(GramaNiladhari gramaNiladhari);
}
