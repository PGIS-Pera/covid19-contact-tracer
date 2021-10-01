package lk.covid19.contact_tracer.asset.district.service;


import lk.covid19.contact_tracer.asset.common_asset.model.enums.Province;
import lk.covid19.contact_tracer.asset.district.entity.District;

import java.util.List;

public interface DistrictService {

  List< District > findAll();

  District findById(Integer id);

  District persist(District district);

  boolean delete(Integer id);

  List< District > search(District district);

  List< District > findByProvince(Province province);

  District findByName(String name);
}
