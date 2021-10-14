package lk.covid19.contact_tracer.asset.treatment_center.service;


import lk.covid19.contact_tracer.asset.treatment_center.entity.TreatmentCenter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface TreatmentCenterService {
   List< TreatmentCenter > findAll();

   TreatmentCenter findById(Integer id);

   TreatmentCenter persist(TreatmentCenter treatmentCenter);

   boolean delete(Integer id);

   List< TreatmentCenter > search(TreatmentCenter treatmentCenter);

   Page< TreatmentCenter > findAllPageable(Pageable pageable);
}
