package lk.covid19.contact_tracer.asset.turn.dao;

import lk.covid19.contact_tracer.asset.turn.entity.Turn;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TurnDao extends JpaRepository< Turn, Integer > {
  Turn findByPersonAndIdentifiedDate(Person personDb, LocalDate identifiedDate);

  List< Turn > findByIdentifiedDateIsBetween(LocalDate from, LocalDate to);

  List< Turn > findByPerson(Person person);
}
