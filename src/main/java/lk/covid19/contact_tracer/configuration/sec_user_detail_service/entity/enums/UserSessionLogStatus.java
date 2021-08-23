package lk.covid19.contact_tracer.configuration.sec_user_detail_service.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserSessionLogStatus {
  LOGGED("User Logged"),
  LOGOUT("User Logout"),
  FAILURE("Failure");

  private final String userSessionLogStatus;
}
