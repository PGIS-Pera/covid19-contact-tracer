package lk.covid19.contact_tracer.asset.turn.service;

import lk.covid19.contact_tracer.asset.person.entity.enums.PersonStatus;
import lk.covid19.contact_tracer.asset.turn.entity.Turn;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TurnService {

  List< Turn > findAll();

  Turn findById(Integer id);

  Turn persist(Turn turn);

  boolean delete(Integer id);

  List< Turn > search(Turn turn);

  Turn findByPatientAndIdentifiedDate(Person personDb, LocalDate identifiedDate);

  List< Turn > findByIdentifiedDateIsBetween(LocalDate startDate, LocalDate endDate);

  Page< Turn > findAllPageable(Pageable pageable);

  List< Turn > findByPerson(Person person);

  List< Turn > findByPersonStatus(PersonStatus personStatus);

  List< Turn > findByCreatedAtIsBetween(LocalDateTime form, LocalDateTime to);

  List< Turn > findByCreatedAtIsBetweenAndCreatedBy(LocalDateTime form, LocalDateTime to, String username);

  List< Turn > findByCreatedBy(String username);

  Turn findLastTurn();

  Turn persistPersonStatue(Turn turn);
}
