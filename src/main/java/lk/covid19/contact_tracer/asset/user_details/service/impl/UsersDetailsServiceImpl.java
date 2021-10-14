package lk.covid19.contact_tracer.asset.user_details.service.impl;


import lk.covid19.contact_tracer.asset.common_asset.model.enums.StopActive;
import lk.covid19.contact_tracer.asset.user_details.dao.UserDetailsDao;
import lk.covid19.contact_tracer.asset.user_details.entity.UserDetails;
import lk.covid19.contact_tracer.asset.user_details_files.service.UserDetailsFilesService;
import lk.covid19.contact_tracer.asset.user_details.service.UsersDetailsService;
import lk.covid19.contact_tracer.util.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
//@CacheConfig( cacheNames = "userDetails" )
@RequiredArgsConstructor
public class UsersDetailsServiceImpl implements UsersDetailsService {

  private final UserDetailsDao userDetailsDao;
  private final CommonService commonService;
  private final UserDetailsFilesService userDetailsFilesService;

  //@Cacheable
  public List< UserDetails > findAll() {
    List< UserDetails > userDetailsList = new ArrayList<>();
    for ( UserDetails userDetails : userDetailsDao.findAll(Sort.by(Sort.Direction.DESC, "id"))
        .stream()
        .filter(x -> StopActive.ACTIVE.equals(x.getStopActive()))
        .collect(Collectors.toList())
    ) {
      userDetails.setFileInfo(userDetailsFilesService.userDetailsFileDownloadLinks(userDetails));
      userDetailsList.add(userDetails);
    }
    return userDetailsList;
  }

  //@Cacheable
  public UserDetails findById(Integer id) {
    return userDetailsDao.getById(id);
  }

  //@Caching( evict = {//@CacheEvict( value = "userDetails", allEntries = true )},      put = {@CachePut( value = "userDetails", key = "#userDetails.id" )} )
  public UserDetails persist(UserDetails userDetails) {
    if ( userDetails.getId() == null ) {
      if ( userDetailsDao.findFirstByOrderByIdDesc() != null ) {
        userDetails.setStopActive(StopActive.ACTIVE);
      }
    }
    if ( userDetails.getId() == null ) {
      //if there is not item in db
      if ( userDetailsDao.findFirstByOrderByIdDesc() == null ) {
        //need to generate new one
        userDetails.setNumber("LKCU" + commonService.numberAutoGen(null).toString());
      } else {
        //if there is item in db need to get that item's code and increase its value
        String previousCode = userDetailsDao.findFirstByOrderByIdDesc().getNumber().substring(4);
        userDetails.setNumber("LKCU" + commonService.numberAutoGen(previousCode).toString());
      }
    }
    userDetails.setMobileOne(userDetails.getMobileOne() != null ?
                                 commonService.phoneNumberLengthValidator(userDetails.getMobileOne()) : null);
    userDetails.setMobileTwo(userDetails.getMobileTwo() != null ?
                                 commonService.phoneNumberLengthValidator(userDetails.getMobileTwo()) : null);
    userDetails.setLand(userDetails.getLand() != null ?
                            commonService.phoneNumberLengthValidator(userDetails.getLand()) : null);

    return userDetailsDao.save(userDetails);
  }

  //@CacheEvict( allEntries = true )
  public boolean delete(Integer id) {
    UserDetails userDetails = userDetailsDao.getById(id);
    userDetails.setStopActive(StopActive.STOP);
    userDetailsDao.save(userDetails);
    return false;
  }

  //@Cacheable
  public List< UserDetails > search(UserDetails userDetails) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< UserDetails > employeeExample = Example.of(userDetails, matcher);
    return userDetailsDao.findAll(employeeExample);
  }

  //@Cacheable
  public UserDetails findByNic(String nic) {
    return userDetailsDao.findByNic(nic);
  }

  public UserDetails findLastUserDetails() {
    return userDetailsDao.findFirstByOrderByIdDesc();
  }
}
