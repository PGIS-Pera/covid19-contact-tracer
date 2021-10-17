package lk.covid19.contact_tracer;


import lk.covid19.contact_tracer.asset.common_asset.model.Mail;
import lk.covid19.contact_tracer.util.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class EmailSendController {
  private static final Logger log = LoggerFactory.getLogger(EmailSendController.class);
  private final EmailService emailService;

  @GetMapping( "/sample" )
  public String sample() throws Exception {

    log.info("START... Sending email");

    Mail mail = new Mail();
    mail.setFrom("asakahatapitiya@gmail.com");//replace with your desired email
    mail.setMailTo("asakahatapitiya@gmail.com");//replace with your desired email
    mail.setSubject("Email with Spring boot and thymeleaf template!");

    Map< String, Object > model = new HashMap<>();
    model.put("name", "Developer!");
    model.put("location", "United States");
    model.put("sign", "Java Developer");
    mail.setProps(model);

    emailService.sendEmail(mail);
    log.info("END... Email sent success");
    return "successMessage/success";
  }

}
