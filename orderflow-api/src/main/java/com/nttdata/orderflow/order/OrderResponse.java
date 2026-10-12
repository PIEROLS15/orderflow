package com.nttdata.orderflow.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(UUID id, UUID customerId, String customerEmail, String status, BigDecimal totalAmount,
                            String externalProfileId, LocalDateTime createdAt, LocalDateTime confirmedAt,
                            List<OrderItemResponse> items) {
  static OrderResponse from(OrderEntity entity) {
    return new OrderResponse(entity.id, entity.customer.id, entity.customer.email, entity.status, entity.totalAmount,
        entity.externalProfileId, entity.createdAt, entity.confirmedAt,
        entity.items.stream().map(OrderItemResponse::from).toList());
  }
}
