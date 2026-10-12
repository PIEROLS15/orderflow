package com.nttdata.orderflow.order;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrderEventLogRepository implements PanacheRepositoryBase<OrderEventLogEntity, UUID> {
  public List<OrderEventLogEntity> findByOrderId(UUID orderId) {
    return list("orderId", orderId);
  }
}
