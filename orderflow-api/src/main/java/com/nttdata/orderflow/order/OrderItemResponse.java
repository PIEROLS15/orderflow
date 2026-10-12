package com.nttdata.orderflow.order;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(UUID productId, String productName, int quantity, BigDecimal unitPrice, BigDecimal subtotal) {
  static OrderItemResponse from(OrderItemEntity entity) {
    return new OrderItemResponse(entity.product.id, entity.product.name, entity.quantity, entity.unitPrice, entity.subtotal);
  }
}
