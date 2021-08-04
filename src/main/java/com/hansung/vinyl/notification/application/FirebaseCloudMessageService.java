package com.hansung.vinyl.notification.application;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.hansung.vinyl.notification.dto.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class FirebaseCloudMessageService {
    private static final String FCM_PRIVATE_KEY_PATH = "firebase/vinyl-fcm-firebase-adminsdk.json";

    @PostConstruct
    public void initialize() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials
                            .fromStream(new ClassPathResource(FCM_PRIVATE_KEY_PATH).getInputStream()))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("[FirebaseCloudMessageService][sendMessageByTopic] Firebase application has been initialized");
            }
        } catch (IOException exception) {
            log.error("[FirebaseCloudMessageService][sendMessageByTopic] FCM 초기화 실패", exception);
        }
    }

    public void sendMessageByTopic(NotificationRequest notificationRequest)
            throws ExecutionException, InterruptedException {
        Message message = buildMessage(notificationRequest);
        String response = FirebaseMessaging.getInstance().sendAsync(message).get();
        log.info("[FirebaseCloudMessageService][sendMessageByTopic] sent message = {}", response);
    }

    private Message buildMessage(NotificationRequest notificationRequest) {
        return Message.builder()
                .setTopic(notificationRequest.getTopic())
                .setNotification(Notification.builder()
                        .setTitle(notificationRequest.getTitle())
                        .setBody(notificationRequest.getBody())
                        .setImage(notificationRequest.getImageURL())
                        .build())
                .build();
    }
}
