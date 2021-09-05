package lk.covid19.contact_tracer.asset.attempt.service;

import lk.covid19.contact_tracer.asset.attempt.entity.Attempt;
import lk.covid19.contact_tracer.asset.person.entity.Person;

import java.time.LocalDate;
import java.util.List;

public interface AttemptService {

  List< Attempt > findAll();

  Attempt findById(Integer id);

  Attempt persist(Attempt attempt);

  boolean delete(Integer id);

  List< Attempt > search(Attempt attempt);

  Attempt findByPatientAndIdentifiedDate(Person personDb, LocalDate identifiedDate);

  List< Attempt > findByIdentifiedDateIsBetween(LocalDate startDate, LocalDate endDate);
}
