package com.yulikexuan.messaging.service;


import com.yulikexuan.messaging.model.ChangeNotification;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;


@Slf4j
@Service
public class NotificationListener {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${sqs.id}")
    private String queueUrl;

    private SqsClient sqsClient;

    private void init() {
        sqsClient = SqsClient.builder()
                .region(Region.of(awsRegion))
                .build();
    }

    public ChangeNotification receiveMessage(
            @NonNull final ChangeNotification changeNotification) {

        try {

            init();

            ReceiveMessageRequest receiveMessageRequest =
                    ReceiveMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .maxNumberOfMessages(1)
                            .build();

            Message message = sqsClient.receiveMessage(
                    receiveMessageRequest).messages().get(0);

            // Gson gson = new Gson();
            // SNSMessage snsMessage = gson.fromJson(message.body(), SNSMessage.class);

            changeNotification.setReceivedMessage(message.body());

            DeleteMessageRequest delRequest = DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(message.receiptHandle())
                    .build();

            sqsClient.deleteMessage(delRequest);

        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }

        return changeNotification;
    }
}
