package lk.covid19.contact_tracer.asset.stay_location.service;


import lk.covid19.contact_tracer.asset.stay_location.entity.StayLocation;

import java.util.List;


public interface StayLocationService {

  List< StayLocation > findAll();

  StayLocation findById(Integer id);

  StayLocation persist(StayLocation stayLocation);

  boolean delete(Integer id);

  List< StayLocation > search(StayLocation stayLocation);

}
