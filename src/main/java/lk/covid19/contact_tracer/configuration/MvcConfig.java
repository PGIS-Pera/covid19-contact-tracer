package lk.covid19.contact_tracer.configuration;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.TimeZone;

@Configuration
@EnableWebMvc
@EnableCaching
public class MvcConfig implements WebMvcConfigurer {
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/login")
        .setViewName("login/login");
    registry.addViewController("/mainWindow")
        .setViewName("mainWindow");
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/img/**")
        .addResourceLocations("classpath:/static/img/");
    registry.addResourceHandler("/css/**")
        .addResourceLocations("classpath:/static/css/");
    registry.addResourceHandler("/js/**")
        .addResourceLocations("classpath:/static/js/");
    registry.addResourceHandler("/webjars/**")
        .addResourceLocations("/webjars/");

  }

  //time zone set to
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(TimeZone.getTimeZone("Asia/Colombo"));
  }

  //to enable Cache in spring boot
  @Bean
  public CacheManager cacheManager() {
    return new ConcurrentMapCacheManager();
  }

  @Bean
  public KeyGenerator multiplyKeyGenerator() {
    return (Object target, Method method, Object... params) -> method.getName() + "_" + Arrays.toString(params);
  }


  //Manage all errors
  @ControllerAdvice
  public static class ErrorController {

    private final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler( Throwable.class )
    @ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
    public String exception(final Throwable throwable, Model model) {
      logger.error("Exception during execution of SpringSecurity application", throwable);
      String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
      model.addAttribute("errorMessage", errorMessage);
      return "error/error";
    }
  }

}