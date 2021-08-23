package lk.covid19.contact_tracer.configuration.sec_user_detail_service;


import lk.covid19.contact_tracer.asset.user.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
public class CustomerUserDetails implements UserDetails {

  private User user;


  @Override
  @Transactional( readOnly = true )
  public Collection< ? extends GrantedAuthority > getAuthorities() {
    return user.getRoles()
        .stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toUpperCase()))
        .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return user.isEnabled();
  }
}