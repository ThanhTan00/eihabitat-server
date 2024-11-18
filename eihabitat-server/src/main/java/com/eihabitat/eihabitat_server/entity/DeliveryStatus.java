package com.eihabitat.eihabitat_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryStatus {
    private String messageId;
    private String status;  // DELIVERED, SEEN, etc.
}