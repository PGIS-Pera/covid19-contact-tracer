package lk.covid19.contact_tracer.util.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityAuditor implements AuditorAware< String > {
  @Override
  public Optional< String > getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if ( authentication != null ) {
      return Optional.of(authentication.getName());
    } else {
      return Optional.of("admin");
    }

  }
}
