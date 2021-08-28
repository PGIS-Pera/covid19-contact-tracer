package lk.covid19.contact_tracer.asset.attempt.dao;

import lk.covid19.contact_tracer.asset.attempt.entity.Attempt;
import lk.covid19.contact_tracer.asset.person.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttemptDao extends JpaRepository< Attempt, Integer > {
  Attempt findByPatientAndIdentifiedDate(Person personDb, LocalDate identifiedDate);

  List< Attempt > findByIdentifiedDateIsBetween(LocalDate from, LocalDate to);
}
