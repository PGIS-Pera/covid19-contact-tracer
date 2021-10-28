package lk.covid19.contact_tracer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ContactTracerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ContactTracerApplication.class, args);
  }

}
