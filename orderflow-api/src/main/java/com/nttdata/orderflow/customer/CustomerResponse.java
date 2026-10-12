package com.nttdata.orderflow.customer;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerResponse(UUID id, String firstName, String lastName, String email, String documentNumber,
                               String status, LocalDateTime createdAt) {
  static CustomerResponse from(CustomerEntity entity) {
    return new CustomerResponse(entity.id, entity.firstName, entity.lastName, entity.email, entity.documentNumber,
        entity.status, entity.createdAt);
  }
}
