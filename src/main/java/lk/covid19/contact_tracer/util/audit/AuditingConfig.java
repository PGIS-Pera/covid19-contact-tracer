package lk.covid19.contact_tracer.util.audit;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration
public class AuditingConfig {

  @Bean
  public AuditorAware< String > createAuditorProvider() {
    return new SecurityAuditor();
  }


}
