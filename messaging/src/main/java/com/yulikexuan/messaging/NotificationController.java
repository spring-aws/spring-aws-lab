package com.yulikexuan.messaging;


import com.yulikexuan.messaging.model.ChangeNotification;
import com.yulikexuan.messaging.model.NotificationRequest;
import com.yulikexuan.messaging.service.NotificationListener;
import com.yulikexuan.messaging.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/notification")
@AllArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    private final NotificationListener notificationListener;

    @PostMapping(path = "/send",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<ChangeNotification> sendNotification(
            @RequestBody NotificationRequest notificationRequest) {

        ChangeNotification sent = notificationService.sendNotification(
                notificationRequest);

        ChangeNotification received = notificationListener.receiveMessage(sent);

        return ResponseEntity.ok(received);
    }

}