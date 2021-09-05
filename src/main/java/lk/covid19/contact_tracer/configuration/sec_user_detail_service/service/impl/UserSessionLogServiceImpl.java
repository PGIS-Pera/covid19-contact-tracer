package lk.covid19.contact_tracer.configuration.sec_user_detail_service.service.impl;

import lk.covid19.contact_tracer.asset.user.entity.User;
import lk.covid19.contact_tracer.configuration.sec_user_detail_service.dao.UserSessionLogDao;
import lk.covid19.contact_tracer.configuration.sec_user_detail_service.entity.UserSessionLog;
import lk.covid19.contact_tracer.configuration.sec_user_detail_service.entity.enums.UserSessionLogStatus;
import lk.covid19.contact_tracer.configuration.sec_user_detail_service.service.UserSessionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig( cacheNames = {"userSessionLog"} )
public class UserSessionLogServiceImpl implements UserSessionLogService {
  private final UserSessionLogDao userSessionLogDao;

  @Autowired
  public UserSessionLogServiceImpl(UserSessionLogDao userSessionLogDao) {
    this.userSessionLogDao = userSessionLogDao;
  }


  @Cacheable
  public List< UserSessionLog > findAll() {
    return userSessionLogDao.findAll();
  }

  @Cacheable
  public UserSessionLog findById(Integer id) {
    return userSessionLogDao.getById(id);
  }


  @Caching( evict = {@CacheEvict( value = "userSessionLog", allEntries = true )},
      put = {@CachePut( value = "userSessionLog", key = "#userSessionLog.id" )} )
  public UserSessionLog persist(UserSessionLog userSessionLog) {
    return userSessionLogDao.save(userSessionLog);
  }


  public boolean delete(Integer id) {
    // can not be implement

    return true;
  }

  public void delete(UserSessionLog userSessionLog) {
    userSessionLogDao.delete(userSessionLog);
  }


  public List< UserSessionLog > search(UserSessionLog userSessionLog) {
    return null;
  }

  @Cacheable
  public UserSessionLog findByUserAndUserSessionLogStatus(User user, UserSessionLogStatus userSessionLogStatus) {
    return userSessionLogDao.findByUserAndUserSessionLogStatus(user, userSessionLogStatus);
  }
}
