package lk.covid19.contact_tracer.asset.ds_office.service;


import lk.covid19.contact_tracer.asset.district.entity.District;
import lk.covid19.contact_tracer.asset.ds_office.entity.DsOffice;

import java.util.List;


public interface DsOfficeService {

  List< DsOffice > findAll();

  DsOffice findById(Integer id);

  DsOffice persist(DsOffice dsOffice);

  boolean delete(Integer id);

  List< DsOffice > search(DsOffice dsOffice);

  boolean isAgOfficePresent(DsOffice dsOffice);

  List< DsOffice > findByDistrict(District district);

  DsOffice findByName(String name);
  //todo total count according to person status
  //todo total count according to gs division level
}
