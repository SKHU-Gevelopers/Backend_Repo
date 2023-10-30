package site.unimeet.unimeetbackend.api;

import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class Push {

    @GetMapping("/push")
    public String push(
            @RequestParam("registrationToken") String registrationToken
    ) throws FirebaseMessagingException {
        // This registration token comes from the client FCM SDKs.

        // See documentation on defining a message payload.
        Message message = Message.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .setToken(registrationToken)
                .setWebpushConfig(WebpushConfig.builder()
                        .setNotification(new WebpushNotification(
                                "Web Push 알림 제목",
                                "Web Push 알림 본문"))
                        .build())
                .build();

        // Send a message to the device corresponding to the provided
        // registration token.
        String response = FirebaseMessaging.getInstance().send(message);

        // Response is a message ID string.
        return "Successfully sent message: " + response;
    }

    @GetMapping("/push-log")
    public void pushLog(
            @RequestParam("registrationToken") String registrationToken
    ) {
        log.info("@@@@@@@@@@@@@@@@@@@@@@@");
        log.info("registrationToken: {}", registrationToken);
        log.info("@@@@@@@@@@@@@@@@@@@@@@@");
    }
}
