package lk.covid19.contact_tracer.util.schedule;


import lk.covid19.contact_tracer.asset.common_asset.model.Mail;
import lk.covid19.contact_tracer.asset.report.model.UserVsReportDTO;
import lk.covid19.contact_tracer.asset.report.service.ReportService;
import lk.covid19.contact_tracer.util.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class PHIEmailSchedule {
  private final ReportService reportService;
  private final EmailService emailService;

  @Scheduled( cron = "0 0 0 * * *" )
  void fourthSchedule() {
    List< UserVsReportDTO > userVsReportDTOS = reportService.userVsReport();
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
  }

}
