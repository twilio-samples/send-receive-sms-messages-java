<!-- markdownlint-disable MD013 -->

# Send & receive SMS messages with Twilio and Java

Learn how to send and receive SMS with Twilio and Java with this repository.
In just a few lines of code, you can see your phone light up sending and receiving SMS with Twilio and Java.

## Prerequisites

To run the app locally, you need the following:

- Java 25 or later
- [ngrok][ngrok] and a free ngrok account
- A [Twilio account][twilio_signup] with an active phone number that can send SMS
- Some command-line/terminal and [Spring][java_spring] experience would be helpful, but it's not necessary

## Quickstart

First things first, clone or download this repository.

### Send an SMS

Before you can send an SMS, you need to complete the following to launch the Java Spring web application:

1. Make a note of the command below.

   ```bash
   TWILIO_ACCOUNT_SID=<TWILIO_ACCOUNT_SID> TWILIO_AUTH_TOKEN=<TWILIO_AUTH_TOKEN> TWILIO_PHONE_NUMBER=<TWILIO_PHONE_NUMBER> \
        mvn spring-boot:run
   ```

1. Go to the [Twilio Console][twilio_console], then navigate to **Develop > API Key & creds > API Keys & auth tokens > Auth tokens** and find your **Account SID** and **Auth Token**.
1. Copy those values and set them as the values of `<TWILIO_ACCOUNT_SID>`, `<TWILIO_AUTH_TOKEN>`, respectively, in the command above.
1. Then, navigate to **Products & Services > Numbers & senders > Overview > Phone Numbers > Inventory** and copy your Twilio phone number.
1. In the command above, replace `<TWILIO_PHONE_NUMBER>` in the command with your phone number.
1. Run the command above to start the Java Spring application.

Now, to send an SMS:

1. Replace `<YOUR_PHONE_NUMBER>` in the command below with your phone number in [E.164 format][e164_format]

   ```bash
   curl --data-urlencode "recipient=<YOUR_PHONE_NUMBER>" http://localhost:8080/send
   ```

1. Run the command to send an SMS

### Receive an SMS

Before you can receive an SMS, you need to complete the following steps:

1. Start your ngrok server:

   ```bash
   ngrok http 8080
   ```

1. Navigate to **Products & Services > Numbers & senders > Overview > Phone Numbers > Inventory** and click your Twilio phone number.
1. In the **Configuration Details** tab, click Messaging.
1. Click the **Edit details** button
1. Under **How do you want to configure this number?** set the **Select a method** dropdown to "Webhook, TwiML Bin, Function, Studio Flow, Proxy Service"
1. Under **How do you want to set up your primary method?**, set **Select your primary method** to "Use webhooks"
1. In the **A call comes in** row, select the **Webhook** option.
1. Then, paste your ngrok **Forwarding** URL in the **What is your webhook URL?** field followed by "/receive/".
   For example, if your ngrok console shows Forwarding "<https://1aaa-123-45-678-910.ngrok-free.app>", enter "<https://1aaa-123-45-678-910.ngrok-free.app/receive/>".
   - To receive an SMS **without** responding to it, append "no-response" to the URL
   - To receive an SMS and respond to it, append "with-response" to the URL
1. For **Select a method to handle responses**, choose "HTTP POST"
1. Click **Save**.
1. Start the Java Spring web app

   ```bash
   mvn spring-boot:run
   ```

1. With both the Java Spring app and ngrok running, send an SMS to your Twilio phone number, containing whatever message you like.
   If you want a response, try sending "never gonna" as the message.

[java_spring]: https://spring.io
[e164_format]: https://www.twilio.com/docs/glossary/what-e164
[ngrok]: https://ngrok.com/
[twilio_console]: https://console.twilio.com
[twilio_signup]: https://www.twilio.com/try-twilio

<!-- markdownlint-enable MD013 -->
