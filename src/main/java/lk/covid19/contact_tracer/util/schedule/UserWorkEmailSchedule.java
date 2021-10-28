package lk.covid19.contact_tracer.util.schedule;


import lk.covid19.contact_tracer.asset.common_asset.model.AttributeAndCount;
import lk.covid19.contact_tracer.asset.common_asset.model.Mail;
import lk.covid19.contact_tracer.asset.report.model.UserVsReportDTO;
import lk.covid19.contact_tracer.asset.report.service.ReportService;
import lk.covid19.contact_tracer.util.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class UserWorkEmailSchedule {
  private final ReportService reportService;
  private final EmailService emailService;

  private final String FORM = "-(Covid19 Contact Tracer - Sri Lanka - (not reply))";
  private final String SUBJECT = " Day Report and Summary Regarding LK";

  @Scheduled( cron = "0 0 0 * * *", zone = "Asia/Colombo" )
  public void userWorkEmailSchedule() {
    List< UserVsReportDTO > userVsReportDTOS = reportService.userVsReport();
    if ( userVsReportDTOS.size() > 0 ) {
      userVsReportDTOS.forEach((x) -> {
        try {
          Mail mail = new Mail();
          mail.setFrom(FORM);
          mail.setMailTo(x.getEmail());
          mail.setSubject(x.getLocalDate().toString() + SUBJECT);
          mail.setHtmlContent(makeHtml(x.getName(), x.getAttributeAndCounts()));

          emailService.sendEmail(mail);
        } catch ( MessagingException | IOException e ) {
          e.printStackTrace();
        }
      });
    }
  }

  private String makeHtml(String name, List< AttributeAndCount > attributeAndCounts) {
    String content_start = "<head>\n" +
        "    <title>Spring boot eamil teplate with Thymeleaf</title>\n" +
        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
        "    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'/>\n" +
        "    <!-- use the font -->\n" +
        "    <style>\n" +
        "        body {\n" +
        "            font-family: 'Roboto', sans-serif;\n" +
        "            font-size: 48px;\n" +
        "        }\n" +
        "        table,th,td {\n" +
        "            font-family: 'Roboto', sans-serif;\n" +
        "            border: 1px solid black;\n" +
        "        }\n" +
        "        th {\n" +
        "            height: 40px;\n" +
        "        }\n" +
        "        td {\n" +
        "            vertical-align: bottom;\n" +
        "        }\n" +
        "        table {\n" +
        "            width: 100%;\n" +
        "            border-collapse: collapse;\n" +
        "            text-align: center;\n" +
        "            border-collapse: collapse;\n" +
        "        }\n" +
        "        td,th {\n" +
        "            padding: 8px;\n" +
        "        }\n" +
        "    </style>\n" +
        "</head>" +
        "<body >\n" +
        "<div class=\"container-fluid\" >\n" +
        "  <div class=\"container\" >\n" +
        "    <p>" + name + "</p >\n" +
        "    <p >This summary report is generated to according to record which entered by you</p >\n" +
        "  </div >\n" +
        "  <div class=\"container\" >\n" +
        "    <div class=\"row\" >\n" +
        "      <div class=\"container-fluid\" >\n" +
        "        <div class=\"row\" >\n" +
        "          <h3 >Summary Report</h3 >\n" +
        "        </div >\n" +
        "        <div class=\"row\" >\n" +
        "          <table class=\"table table-striped table-inverse table-responsive\" >\n" +
        "            <thead class=\"thead-inverse\" >\n" +
        "            <tr >\n" +
        "              <th >Index</th >\n" +
        "              <th >Status Name</th >\n" +
        "              <th >Record Count</th >\n" +
        "            </tr >\n" +
        "            </thead >\n" +
        "            <tbody >\n";

    StringBuilder tableBody = new StringBuilder();
    for ( int i = 0; i < attributeAndCounts.size(); i++ ) {
      tableBody.append("            <tr >\n" + "              <td >").append(i + 1).append("</td >\n").append("              <td  >").append(attributeAndCounts.get(i).getName()).append("</td >\n").append("              <td  >").append(attributeAndCounts.get(i).getCount()).append("</td >\n").append("            </tr >\n");
    }

    String content_final =
        "            </tbody >\n" +
            "          </table >\n" +
            "        </div >\n" +
            "      </div >\n" +
            "    </div >\n" +
            "  </div >\n" +
            "</div >\n" +
            "<div class=\"container\" >\n" +
            "  <br />\n" +
            "  <br />\n" +
            "  <br />\n" +
            "  Regards,\n" +
            "  <br />\n" +
            "  <br />\n" +
            "  <br />\n" +
            "  <em>" + "Contract Tracer App" + "</em >\n" +
            "  <p >" + "Manage By PGIS </br>\n University of Peradeniya" + "</p >\n" +
            "  <br />\n" +
            "</div >\n" +
            "\n" +
            "  <br />\n" +
            "  <br />\n" +
            "  <br />\n" +
            "  <em > This is a system generated report hence please visited to system to get more details </em >\n" +
            "  <p > Generated At:    " + LocalDateTime.now() + "</p >\n" +
            "</body >";

    return content_start + tableBody + content_final;
  }


}
