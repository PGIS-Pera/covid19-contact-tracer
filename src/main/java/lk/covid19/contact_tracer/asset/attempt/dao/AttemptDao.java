package lk.covid19.contact_tracer.asset.attempt.dao;

import lk.covid19.contact_tracer.asset.attempt.entity.Attempt;
import lk.covid19.contact_tracer.asset.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttemptDao extends JpaRepository< Attempt, Integer > {
  Attempt findByPatientAndIdentifiedDate(Patient patientDb, LocalDate identifiedDate);

  List< Attempt > findByIdentifiedDateIsBetween(LocalDate from, LocalDate to);
}
