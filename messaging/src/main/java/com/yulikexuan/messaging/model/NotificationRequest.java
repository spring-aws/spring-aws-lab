package com.yulikexuan.messaging.model;


import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor(staticName = "of")
public class NotificationRequest {
    private final String message;
}
