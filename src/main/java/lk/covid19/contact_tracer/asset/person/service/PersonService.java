package lk.covid19.contact_tracer.asset.person.service;


import lk.covid19.contact_tracer.asset.person.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;


public interface PersonService {

  Page< Person > findAllPageable(Pageable pageable);

  List< Person > findAll();

  Person findById(Integer id);

  Person persist(Person person);

  boolean delete(Integer id);

  List< Person > search(Person person);

  Person findByNic(String nic);

  Person findLastPatient();

  List< Person > persistList(List< Person > people);

  List< Person > findByTurnIdentifiedDateRange(LocalDate startDate, LocalDate endDate);

  void saveAndTurn(Person person);
}
