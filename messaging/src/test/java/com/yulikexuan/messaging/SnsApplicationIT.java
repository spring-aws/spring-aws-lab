package com.yulikexuan.messaging;


import com.yulikexuan.messaging.model.ChangeNotification;
import com.yulikexuan.messaging.model.NotificationRequest;
import com.yulikexuan.messaging.service.NotificationListener;
import com.yulikexuan.messaging.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;


@Slf4j
@SpringBootTest
class SnsApplicationIT {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private NotificationListener notificationListener;

	private NotificationRequest notificationRequest;

	private Instant instant;

	@BeforeEach
	void setUp() {
		this.instant = Instant.now();
		this.notificationRequest = NotificationRequest.of(
				String.format(">>> Test SNS/SQS at %s", this.instant.toString()));
	}

	@Test
	void able_To_Post_Message_To_SNS_Then_() {
		ChangeNotification changeNotification  =
				notificationService.sendNotification(this.notificationRequest);
		changeNotification = notificationListener.receiveMessage(changeNotification);
		log.info(">>> The sent message is {}", changeNotification.getReceivedMessage());
	}

}