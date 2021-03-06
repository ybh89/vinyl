package com.hansung.vinyl.notification.application;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.hansung.vinyl.account.domain.AccountCreatedEvent;
import com.hansung.vinyl.notification.domain.FcmToken;
import com.hansung.vinyl.notification.domain.FcmTokenRepository;
import com.hansung.vinyl.notification.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {
    private static final String FCM_PRIVATE_KEY_PATH = "firebase/vinyl-fcm-firebase-adminsdk.json";

    private final FcmTokenRepository fcmTokenRepository;

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
            log.error("[FirebaseCloudMessageService][sendMessageByTopic] FCM ????????? ??????", exception);
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

    @Async
    @EventListener
    public void createFcmToken(AccountCreatedEvent accountCreatedEvent) {
        FcmToken fcmToken = new FcmToken(accountCreatedEvent.getAccount().getId(),
                accountCreatedEvent.getMemberInfo().getFcmToken());
        fcmTokenRepository.save(fcmToken);
    }
}
