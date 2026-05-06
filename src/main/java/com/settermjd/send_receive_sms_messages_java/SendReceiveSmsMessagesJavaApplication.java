package com.settermjd.send_receive_sms_messages_java;

import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Body;
import com.twilio.twiml.messaging.Message;
import java.util.Random;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.MediaType;

@SpringBootApplication
@RestController
public class SendReceiveSmsMessagesJavaApplication {
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
    }

    @RequestMapping(method = RequestMethod.POST, path = "/receive/no-response", produces = "application/xml")
    public String noResponse() {
        MessagingResponse twiml = new MessagingResponse.Builder()
                .build();
        return twiml.toXml();
    }

    @PostMapping(path = "/receive/with-response", consumes = {
            MediaType.APPLICATION_FORM_URLENCODED_VALUE }, produces = "application/xml")
    public @ResponseBody String withResponse(@RequestParam("Body") String requestBody) {
        Body smsBody;
        if (requestBody.toLowerCase().equals("never gonna")) {
            Random random = new Random();
            int index = random.nextInt(0, options.length - 1);
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
