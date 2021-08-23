package lk.covid19.contact_tracer.asset.patient_visited_place_time.dao;


import lk.covid19.contact_tracer.asset.patient_visited_place_time.entity.PatientVisitedPlaceTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientVisitedPlaceTimeDao extends JpaRepository< PatientVisitedPlaceTime, Integer > {
}
