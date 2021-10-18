package lk.covid19.contact_tracer;


import lk.covid19.contact_tracer.asset.common_asset.model.Mail;
import lk.covid19.contact_tracer.asset.report.model.UserVsReportDTO;
import lk.covid19.contact_tracer.asset.report.service.ReportService;
import lk.covid19.contact_tracer.util.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class EmailSendController {
  private static final Logger log = LoggerFactory.getLogger(EmailSendController.class);
  private final EmailService emailService;
  private final ReportService reportService;

  private final String FORM = "-(Contact Tracer - Sri Lanka - (not reply))";
  private final String SUBJECT = " Day Report and Summary Regarding LK";

  @GetMapping( "/sample" )
  public String sample() throws Exception {

/*    log.info("START... Sending email");

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
    log.info("END... Email sent success");*/

    List< UserVsReportDTO > userVsReportDTOS = reportService.userVsReport();

    userVsReportDTOS.forEach((x) -> {

      try {
        Mail mail = new Mail();
        mail.setFrom(FORM);
        mail.setMailTo(x.getEmail());//replace with your desired email
        mail.setSubject(x.getLocalDate().toString() + SUBJECT);
        System.out.println(x.toString());

        Map< String, Object > model = new HashMap<>();
        model.put("name", x.getName());
        model.put("userVsReportDetail", x.getAttributeAndCounts());
        model.put("location", "Manage By PGIS \n UOP");
        model.put("sign", "Contract Tracer App");
        mail.setProps(model);


        emailService.sendEmail(mail);
        log.info("END... Email sent success");
      } catch ( MessagingException | IOException e ) {
        e.printStackTrace();
      }
    });

    return "successMessage/success";
  }

}
