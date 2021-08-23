package lk.covid19.contact_tracer.asset.ds_office.dao;


import lk.covid19.contact_tracer.asset.district.entity.District;
import lk.covid19.contact_tracer.asset.ds_office.entity.DsOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DsOfficeDao extends JpaRepository< DsOffice, Integer > {

  List< DsOffice > findByDistrict(District district);

  DsOffice findByName(String name);
}
