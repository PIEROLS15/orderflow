package com.nttdata.orderflow.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(UUID id, String name, String description, BigDecimal price, int stock, boolean active,
                              LocalDateTime createdAt) {
  static ProductResponse from(ProductEntity entity) {
    return new ProductResponse(entity.id, entity.name, entity.description, entity.price, entity.stock, entity.active,
        entity.createdAt);
  }
}
