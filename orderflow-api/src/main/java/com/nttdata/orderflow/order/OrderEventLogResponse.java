package com.nttdata.orderflow.order;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderEventLogResponse(UUID id, UUID orderId, String eventType, String status, String message,
                                    LocalDateTime processedAt) {
  static OrderEventLogResponse from(OrderEventLogEntity entity) {
    return new OrderEventLogResponse(entity.id, entity.orderId, entity.eventType, entity.status, entity.message,
        entity.processedAt);
  }
}
