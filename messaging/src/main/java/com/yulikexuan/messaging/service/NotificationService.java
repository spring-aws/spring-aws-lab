package com.yulikexuan.messaging.service;


import com.yulikexuan.messaging.model.ChangeNotification;
import com.yulikexuan.messaging.model.NotificationRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;


@Slf4j
@Service
public class NotificationService {

    @Value("${sns.topic.arn}")
    private String topicArn;

    @Value("${aws.region}")
    private String awsRegion;

    private SnsClient snsClient;

    private void init() {
        snsClient = SnsClient.builder()
                .region(Region.of(awsRegion))
                .build();
    }

    public ChangeNotification sendNotification(
            @NonNull final NotificationRequest notificationRequest) {

        ChangeNotification changeNotification = ChangeNotification.builder()
                .sentMessage(notificationRequest.getMessage()).build();

        try {
            init();

            PublishRequest request = PublishRequest.builder()
                    .message(notificationRequest.getMessage())
                    .messageGroupId("default")
                    .topicArn(topicArn)
                    .build();

            PublishResponse result = snsClient.publish(request);

            log.info(">>> {} Message sent. Status was {}",
                    result.messageId(), result.sdkHttpResponse().statusCode());

            changeNotification.setId(result.messageId());

        } catch (SnsException e) {
            log.error(e.awsErrorDetails().errorMessage());
        }

        return changeNotification;
    }
}
