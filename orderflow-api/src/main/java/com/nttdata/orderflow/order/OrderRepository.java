package com.nttdata.orderflow.order;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrderRepository implements PanacheRepositoryBase<OrderEntity, UUID> {
  public List<OrderEntity> findByCustomer(UUID customerId) {
    return list("customer.id", customerId);
  }

  public List<OrderEntity> findByStatus(String status) {
    return list("status", status);
  }
}
