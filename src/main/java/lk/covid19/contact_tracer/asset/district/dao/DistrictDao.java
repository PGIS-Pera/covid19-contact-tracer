package lk.covid19.contact_tracer.asset.district.dao;


import lk.covid19.contact_tracer.asset.common_asset.model.enums.Province;
import lk.covid19.contact_tracer.asset.district.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictDao extends JpaRepository< District, Integer > {
  List< District > findByProvince(Province province);

  District findByName(String name);
}
