package lk.covid19.contact_tracer.asset.patient.dao;


import lk.covid19.contact_tracer.asset.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientDao extends JpaRepository< Patient, Integer > {
  Patient findFirstByOrderByIdDesc();

  Patient findByNic(String nic);
}
