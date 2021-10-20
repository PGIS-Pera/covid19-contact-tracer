package lk.covid19.contact_tracer.util.service;


import lk.covid19.contact_tracer.asset.common_asset.model.Mail;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {
  private final JavaMailSender javaMailSender;

  public void sendEmail(String receiverEmail, String subject, String message) throws
      MailException {
    SimpleMailMessage mailMessage = new SimpleMailMessage();


    try {
      mailMessage.setTo(receiverEmail);
      mailMessage.setFrom("-(Contact Tracer - Sri Lanka - (not reply))");
      mailMessage.setSubject(subject);
      mailMessage.setText(message);

      javaMailSender.send(mailMessage);
    } catch ( Exception e ) {
      System.out.println("Email Exception " + e.toString());
    }
  }

  public void sendEmail(Mail mail) throws MessagingException, IOException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message,
                                                     MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                                                     StandardCharsets.UTF_8.name());

    helper.setTo(mail.getMailTo());
    helper.setText(mail.getHtmlContent(), true);
    helper.setSubject(mail.getSubject());
    helper.setFrom(mail.getFrom());

    javaMailSender.send(message);
  }


}
