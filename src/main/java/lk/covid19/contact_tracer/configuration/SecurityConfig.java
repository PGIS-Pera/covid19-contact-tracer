package lk.covid19.contact_tracer.configuration;

import lk.covid19.contact_tracer.configuration.sec_handler.CustomAuthenticationSuccessHandler;
import lk.covid19.contact_tracer.configuration.sec_handler.CustomLogoutSuccessHandler;
import lk.covid19.contact_tracer.configuration.sec_user_detail_service.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final String[] ALL_PERMIT_URL = {"/favicon", "/img/**", "/css/**", "/js/**", "/webjars/**",
      "/login", "/select/**", "/", "/index", "/register/**", "/forgottenPassword", "/patientVisitedPlaceTime/**",
      "/news/**"};

  @Bean
  public UserDetailsServiceImpl userDetailsService() {
    return new UserDetailsServiceImpl();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  /*Session management - bean start*/
  @Bean
  public HttpSessionEventPublisher httpSessionEventPublisher() {
    return new HttpSessionEventPublisher();
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }
  /*Session management - bean end*/

  @Bean
  public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
    return new CustomAuthenticationSuccessHandler();
  }

  @Bean
  public LogoutSuccessHandler customLogoutSuccessHandler() {
    return new CustomLogoutSuccessHandler();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(authenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
//    http.csrf().disable();
//    http.authorizeRequests().antMatchers("/").permitAll();
    http
        .authorizeRequests(
            authorizeRequests ->
                authorizeRequests
                    .antMatchers(ALL_PERMIT_URL).permitAll()
                    .antMatchers("/actuator/**").hasAnyRole("ADMIN")
                    .antMatchers("/district/**").hasAnyRole("ADMIN")
                    .antMatchers("/dsOffice/**").hasAnyRole("ADMIN")
                    .antMatchers("/gramaNiladhari/add").hasAnyRole("ADMIN")
                    .antMatchers("/gramaNiladhari").hasAnyRole("ADMIN", "PHI")
                    .antMatchers("/user/**").hasAnyRole("ADMIN")
                    .antMatchers("/userDetails/**").hasAnyRole("ADMIN")
                    .antMatchers("/locationInteract/**").hasAnyRole("ADMIN", "PHI")
                    .antMatchers("/person/**").hasAnyRole("PHI")
                    .antMatchers("/turn/**").hasAnyRole("PHI")
                    .antMatchers("/report/**").hasAnyRole("ADMIN", "PHI")
                    .antMatchers("/gramaNiladhari/search", "/gramaNiladhari/searchOne", "/patientVisitedPlaceTime" +
                        "/getPlaces").permitAll()
                    .anyRequest()
                    .authenticated())
        // Login form
        .formLogin(
            formLogin ->
                formLogin
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .failureUrl("/login?error")
                    .successHandler(customAuthenticationSuccessHandler())
                  )
        //Logout controlling
        .logout(
            logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(customLogoutSuccessHandler())
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true))
        //remember me
        .rememberMe()
        .key("uniqueAndSecret")
        .tokenValiditySeconds(43200) // token is valid 12 hours
        .and()
        //session management
        .sessionManagement(
            sessionManagement ->
                sessionManagement
                    .sessionFixation()
                    .migrateSession()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .invalidSessionUrl("/login")
                    .maximumSessions(5)
                    .expiredUrl("/login")
                    .sessionRegistry(sessionRegistry()))
        //Cross site disable
        .cors()
        .and()
        .csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling()
        .and()
        .headers()
        .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));

  }
}

