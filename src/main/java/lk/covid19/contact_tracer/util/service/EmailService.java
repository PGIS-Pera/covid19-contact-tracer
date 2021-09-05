package lk.covid19.contact_tracer.util.service;


import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class EmailService {
  private final JavaMailSender javaMailSender;
  // to access application properties entered details
  private final Environment environment;

  public void sendEmail(String receiverEmail, String subject, String message) throws
      MailException {
    SimpleMailMessage mailMessage = new SimpleMailMessage();


    try {
      mailMessage.setTo(receiverEmail);
      mailMessage.setFrom("-(Excise Department - Sri Lanka - (not reply))");
      mailMessage.setSubject(subject);
      mailMessage.setText(message);

      javaMailSender.send(mailMessage);
    } catch ( Exception e ) {
      System.out.println("Email Exception " + e.toString());
    }
  }

  public boolean sendMailWithImage(String receiverEmail, String subject, String fileName) {
    //final String username = "excellenthealthsolution@gmail.com";
    final String username = environment.getProperty("spring.mail.username");
    //final String password = "dinesh2018";
    final String password = environment.getProperty("spring.mail.password");

    // Assuming you are sending email through gmail
    String host = environment.getProperty("spring.mail.host");
    //String host = "smtp.gmail.com";
    String port = environment.getProperty("spring.mail.port");
    //String port = "587";

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", port);

    // Get the Session object.
    Session session = Session.getInstance(props,
                                          new Authenticator() {
                                            protected PasswordAuthentication getPasswordAuthentication() {
                                              return new PasswordAuthentication(username, password);
                                            }
                                          });
    try {
      // Create a default MimeMessage object.
      Message message = new MimeMessage(session);

      // Set From: header field of the header.
      message.setFrom(new InternetAddress(username));

      // Set To: header field of the header.
      message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(receiverEmail));

      // Set Subject: header field
      message.setSubject(subject);

      // Create the message part
      BodyPart messageBodyPart = new MimeBodyPart();

      // Now set the actual message
      messageBodyPart.setText("Please find the attachment");

      // Create a multipart message
      Multipart multipart = new MimeMultipart();

      // Set text message part
      multipart.addBodyPart(messageBodyPart);

      // Part two is attachment
      messageBodyPart = new MimeBodyPart();
      // set file path to include email
      String filename = fileName;
      DataSource source = new FileDataSource(filename);
      messageBodyPart.setDataHandler(new DataHandler(source));
      messageBodyPart.setFileName(filename);
      multipart.addBodyPart(messageBodyPart);

      // Send the complete message parts
      message.setContent(multipart);

      // Send message
      Transport.send(message);

      System.out.println("Sent message successfully....");
      return true;
    } catch ( MessagingException e ) {
      throw new RuntimeException(e);

    }

  }


}
