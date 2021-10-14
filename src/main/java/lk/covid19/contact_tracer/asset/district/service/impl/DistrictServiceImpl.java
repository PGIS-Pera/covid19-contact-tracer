package lk.covid19.contact_tracer.asset.district.service.impl;

import lk.covid19.contact_tracer.asset.common_asset.model.enums.Province;
import lk.covid19.contact_tracer.asset.district.dao.DistrictDao;
import lk.covid19.contact_tracer.asset.district.entity.District;
import lk.covid19.contact_tracer.asset.district.service.DistrictService;
import lk.covid19.contact_tracer.util.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
//@CacheConfig( cacheNames = "district" )
@RequiredArgsConstructor
public class DistrictServiceImpl implements DistrictService {
  private final DistrictDao districtDao;
  private final CommonService commonService;

  //@Cacheable
  public List< District > findAll() {
    return districtDao.findAll();
  }

  //@Cacheable
  public District findById(Integer id) {
    return districtDao.getById(id);
  }

  //@Caching( evict = {//@CacheEvict( value = "district", allEntries = true )},      put = {@CachePut( value = "district", key = "#district.id" )} )
  public District persist(District district) {
    district.setName(commonService.stringCapitalize(district.getName()));
    return districtDao.save(district);
  }

  //@CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    districtDao.deleteById(id);
    return false;
  }

  //@Cacheable
  public List< District > search(District district) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< District > districtExample = Example.of(district, matcher);
    return districtDao.findAll(districtExample);
  }


  //@Cacheable
  public List< District > findByProvince(Province province) {
    return districtDao.findByProvince(province);
  }


  public District findByName(String name) {
    return districtDao.findByName(name);
  }
}

