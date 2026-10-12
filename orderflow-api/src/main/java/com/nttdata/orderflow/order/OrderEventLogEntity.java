package com.nttdata.orderflow.order;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_order_event_logs")
public class OrderEventLogEntity extends PanacheEntityBase {
  @Id
  @GeneratedValue
  public UUID id;

  @Column(name = "order_id", nullable = false)
  public UUID orderId;

  @Column(name = "event_type", nullable = false)
  public String eventType;

  @Column(nullable = false)
  public String status;

  public String message;

  @Column(name = "processed_at", nullable = false)
  public LocalDateTime processedAt = LocalDateTime.now();

  @Column(name = "created_at", nullable = false)
  public LocalDateTime createdAt = LocalDateTime.now();
}
