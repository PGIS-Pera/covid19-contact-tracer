package lk.covid19.contact_tracer.asset.ds_office.service.impl;


import lk.covid19.contact_tracer.asset.district.entity.District;
import lk.covid19.contact_tracer.asset.ds_office.dao.DsOfficeDao;
import lk.covid19.contact_tracer.asset.ds_office.entity.DsOffice;
import lk.covid19.contact_tracer.asset.ds_office.service.DsOfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//@CacheConfig( cacheNames = "dsOffice" )
@RequiredArgsConstructor
public class DsOfficeServiceImpl implements DsOfficeService {
  private final DsOfficeDao dsOfficeDao;


  //@Cacheable
  public List< DsOffice > findAll() {
    return dsOfficeDao.findAll();
  }

  //@Cacheable
  public DsOffice findById(Integer id) {
    return dsOfficeDao.getById(id);
  }

  //@Caching( evict = {//@CacheEvict( value = "dsOffice", allEntries = true )},      put = {@CachePut( value = "dsOffice", key = "#dsOffice.id" )} )
  public DsOffice persist(DsOffice dsOffice) {
    return dsOfficeDao.save(dsOffice);
  }

  //@CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    dsOfficeDao.deleteById(id);
    return false;
  }

  //@Cacheable
  public List< DsOffice > search(DsOffice dsOffice) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< DsOffice > agOfficeExample = Example.of(dsOffice, matcher);
    return dsOfficeDao.findAll(agOfficeExample);
  }

  public boolean isAgOfficePresent(DsOffice dsOffice) {
    return dsOfficeDao.equals(dsOffice);
  }

  public List< DsOffice > findByDistrict(District district) {
    return dsOfficeDao.findByDistrict(district);
  }


  public DsOffice findByName(String name) {
    return dsOfficeDao.findByName(name);
  }
}
