package com.yulikexuan.messaging.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChangeNotification {
    String id;
    String sentMessage;
    String receivedMessage;
}
