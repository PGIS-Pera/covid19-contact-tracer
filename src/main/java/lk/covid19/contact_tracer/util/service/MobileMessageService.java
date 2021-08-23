package lk.covid19.contact_tracer.util.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;


@Service
public class MobileMessageService {
  // Find your Account Sid and Token at twilio.com/user/account
  public static final String ACCOUNT_SID = "ACbb4e6d5d3af74f779d629c8f8f5e8862";
  public static final String AUTH_TOKEN = "fbb94f881a36e1a66228de2ad7a57efd";


  public void sendSMS(String number, String messageBody) throws Exception {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    Message message = Message
        .creator(new PhoneNumber(number), new PhoneNumber("+14159917342"),
                 messageBody)
        .create();

    System.out.println("Message " + message.getSid());
  }
}
