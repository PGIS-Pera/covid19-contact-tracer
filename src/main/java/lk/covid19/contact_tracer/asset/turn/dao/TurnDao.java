package lk.covid19.contact_tracer.asset.turn.dao;

import lk.covid19.contact_tracer.asset.person.entity.enums.PersonStatus;
import lk.covid19.contact_tracer.asset.turn.entity.Turn;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TurnDao extends JpaRepository< Turn, Integer > {
  Turn findByPersonAndIdentifiedDate(Person personDb, LocalDate identifiedDate);

  List< Turn > findByIdentifiedDateIsBetween(LocalDate from, LocalDate to);

  List< Turn > findByPerson(Person person);

  List< Turn > findByPersonStatus(PersonStatus personStatus);

  List< Turn > findByCreatedAtIsBetween(LocalDateTime form, LocalDateTime to);

  List< Turn > findByCreatedAtIsBetweenAndCreatedBy(LocalDateTime form, LocalDateTime to, String username);

  List< Turn > findByCreatedBy(String username);

  Turn findFirstByOrderByIdDesc();
}
