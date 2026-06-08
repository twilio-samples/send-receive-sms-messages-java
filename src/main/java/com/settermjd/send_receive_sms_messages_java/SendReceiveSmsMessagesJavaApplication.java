package com.settermjd.send_receive_sms_messages_java;

import com.twilio.Twilio;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import com.twilio.type.PhoneNumber;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SendReceiveSmsMessagesJavaApplication {
    private static String accountSid;
    private static String authToken;
    private static String phoneNumber;

    private Logger logger = LogManager.getLogger(SendReceiveSmsMessagesJavaApplication.class);

    @Value("${twilio.account_sid}")
    public void setAccountSid(String accountSid)
    {
        SendReceiveSmsMessagesJavaApplication.accountSid = accountSid;
    }

    @Value("${twilio.auth_token}")
    public void setAuthToken(String authToken)
    {
        SendReceiveSmsMessagesJavaApplication.authToken = authToken;
    }

    @Value("${twilio.phone_number}")
    public void setSenderPhoneNumber(String phoneNumber)
    {
        SendReceiveSmsMessagesJavaApplication.phoneNumber = phoneNumber;
    }

    private static final String defaultOption = "I just wanna tell you how I'm feeling - Gotta make you understand";
    private static final String[] options = {
            "give you up",
            "let you down",
            "make you cry",
            "run around and desert you",
            "say goodbye",
            "tell a lie, and hurt you"
    };

    public static void main(String[] args) {
        SpringApplication.run(SendReceiveSmsMessagesJavaApplication.class, args);

        // Initialise the Twilio client
        Twilio.init(accountSid, authToken);
    }

    /**
     * This route sends an SMS to the designated phone number.
     *
     * @see https://www.twilio.com/docs/messaging/quickstart
     */
    @PostMapping(
        path = "/send", 
        consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
    )
    public @ResponseBody String sendSMS(@RequestParam("recipient") String recipient) {
        com.twilio.rest.api.v2010.account.Message message = com.twilio.rest.api.v2010.account.Message
            .creator(new PhoneNumber(recipient),
                    new PhoneNumber(phoneNumber),
                    "This is the ship that made the Kessel Run in fourteen parsecs?")
            .create();

        logger.info("SMS has been sent. Its SID is: {} and status is: {}", message.getSid(), message.getStatus());

        return message.getBody();
    }

    /**
     * This route does not reply to an incoming SMS.
     *
     * The route's response will contain TwiML that provides no further instructions to Twilio.
     * In addition the response's status code will be an HTTP 200 OK, and the response will have
     * the Content-Type header set to "application/xml; charset=utf-8".
     *
     * @see https://www.twilio.com/docs/messaging/twiml/message
     */
    @PostMapping(
        path = "/receive/no-response", 
        produces = "application/xml"
    )
    public String noResponse() {
        MessagingResponse twiml = new MessagingResponse.Builder()
                .build();
        return twiml.toXml();
    }

    /**
     * This route replies to the incoming SMS.
     *
     * If the request's form data contains an element named "Body" with the value "never gonna",
     * the function's response will contain TwiML that instructs Twilio to send a reply SMS to
     * the sender of the original SMS with a line from Rick Astley's hit "Never Gonna Give You Up".
     * Otherwise it sends the same, stock line from the same song.
     *
     * In addition the response's status code will be an HTTP 200 OK, and the response will have
     * the Content-Type header set to "application/xml; charset=utf-8".
     *
     * @see https://www.twilio.com/docs/messaging/twiml/message
     */
    @PostMapping(path = "/receive/with-response", consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE }, produces = "application/xml")
    public @ResponseBody String withResponse(@RequestParam("Body") String requestBody) {
        Body smsBody;
        if (requestBody.equalsIgnoreCase("never gonna")) {
            int index = ThreadLocalRandom.current().nextInt(options.length);
            smsBody = new Body.Builder(options[index])
                    .build();
        } else {
            smsBody = new Body.Builder(defaultOption)
                    .build();
        }

        Message sms = new Message.Builder()
                .body(smsBody)
                .build();
        MessagingResponse twiml = new MessagingResponse.Builder()
                .message(sms)
                .build();

        return twiml.toXml();
    }
}
