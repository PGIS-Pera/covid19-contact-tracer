package lk.covid19.contact_tracer.configuration.sec_user_detail_service.dao;

import lk.covid19.contact_tracer.asset.user.entity.User;
import lk.covid19.contact_tracer.configuration.sec_user_detail_service.entity.UserSessionLog;
import lk.covid19.contact_tracer.configuration.sec_user_detail_service.entity.enums.UserSessionLogStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionLogDao extends JpaRepository< UserSessionLog, Integer > {
  UserSessionLog findByUserAndUserSessionLogStatus(User user, UserSessionLogStatus userSessionLogStatus);
}
