package lk.covid19.contact_tracer.util.schedule;


import lk.covid19.contact_tracer.asset.common_asset.model.Mail;
import lk.covid19.contact_tracer.asset.report.model.UserVsReportDTO;
import lk.covid19.contact_tracer.asset.report.service.ReportService;
import lk.covid19.contact_tracer.util.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class PHIEmailSchedule {
  private final ReportService reportService;
  private final EmailService emailService;

  private final String FORM = "-(Contact Tracer - Sri Lanka - (not reply))";
  private final String SUBJECT = " Day Report and Summary Regarding LK";

  @Scheduled( cron = "0 0 0 * * *" )
  void fourthSchedule() {
    List< UserVsReportDTO > userVsReportDTOS = reportService.userVsReport();

    userVsReportDTOS.forEach((x) -> {

      Mail mail = new Mail();
      mail.setFrom(FORM);
      mail.setMailTo(x.getEmail());//replace with your desired email
      mail.setSubject(x.getLocalDate().toString() + SUBJECT);

      Map< String, Object > model = new HashMap<>();
      model.put("name", x.getName());
      model.put("userVsReportDetail", x.getAttributeAndCounts());
      model.put("location", "Manage By PGIS \n UOP");
      model.put("sign", "Contract Tracer App");
      mail.setProps(model);

      try {
        emailService.sendEmail(mail);
      } catch ( MessagingException | IOException e ) {
        e.printStackTrace();
      }
    });

  }

}
