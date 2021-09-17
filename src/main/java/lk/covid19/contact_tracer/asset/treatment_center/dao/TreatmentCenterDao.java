package lk.covid19.contact_tracer.asset.treatment_center.dao;


import lk.covid19.contact_tracer.asset.treatment_center.entity.TreatmentCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentCenterDao extends JpaRepository< TreatmentCenter, Integer > {
}
