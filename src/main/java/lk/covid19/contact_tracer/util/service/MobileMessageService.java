package lk.covid19.contact_tracer.util.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class MobileMessageService {

  // Find your Account Sid and Token at twilio.com/user/account
  @Value( "${twilio.ACCOUNT_SID}" )
  private String ACCOUNT_SID;

  @Value( "${twilio.AUTH_TOKEN}" )
  private String AUTH_TOKEN;

  @Value( "${twilio.PHONE_NUMBER}" )
  private String MOBILE_NUMBER;


  public void sendSMS(String number, String messageBody) throws Exception {
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    Message message = Message
        .creator(new PhoneNumber(number), new PhoneNumber(MOBILE_NUMBER),
                 messageBody)
        .create();

    System.out.println("Message " + message.getSid());
  }
}
