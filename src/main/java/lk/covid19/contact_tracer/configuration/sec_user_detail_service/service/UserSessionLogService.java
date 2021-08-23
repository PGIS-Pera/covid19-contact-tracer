package lk.covid19.contact_tracer.configuration.sec_user_detail_service.service;

import lk.covid19.contact_tracer.asset.user.entity.User;
import lk.covid19.contact_tracer.configuration.sec_user_detail_service.entity.UserSessionLog;
import lk.covid19.contact_tracer.configuration.sec_user_detail_service.entity.enums.UserSessionLogStatus;

import java.util.List;

public interface UserSessionLogService {
  List< UserSessionLog > findAll();

  UserSessionLog findById(Integer id);

  boolean delete(Integer id);

  UserSessionLog persist(UserSessionLog userSessionLog);

  void delete(UserSessionLog userSessionLog);

  UserSessionLog findByUserAndUserSessionLogStatus(User user, UserSessionLogStatus userSessionLogStatus);

}
