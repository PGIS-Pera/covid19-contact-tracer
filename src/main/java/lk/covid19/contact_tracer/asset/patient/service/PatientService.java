package lk.covid19.contact_tracer.asset.patient.service;


import lk.covid19.contact_tracer.asset.patient.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;


public interface PatientService {

  Page< Patient > findAllPageable(Pageable pageable);

  List< Patient > findAll();

  Patient findById(Integer id);

  Patient persist(Patient patient);

  boolean delete(Integer id);

  List< Patient > search(Patient patient);

  Patient findByNic(String nic);

  Patient findLastPatient();

  List< Patient > persistList(List< Patient > patients);

  List< Patient > findByAttemptIdentifiedDateRange(LocalDate startDate, LocalDate endDate);
}
