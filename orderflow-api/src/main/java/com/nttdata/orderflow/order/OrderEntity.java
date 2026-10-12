package com.nttdata.orderflow.order;

import com.nttdata.orderflow.customer.CustomerEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_orders")
public class OrderEntity extends PanacheEntityBase {
  @Id
  @GeneratedValue
  public UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  public CustomerEntity customer;

  @Column(nullable = false)
  public String status = OrderStatus.CREADO;

  @Column(name = "total_amount", nullable = false)
  public BigDecimal totalAmount = BigDecimal.ZERO;

  @Column(name = "external_profile_id")
  public String externalProfileId;

  @Column(name = "created_at", nullable = false)
  public LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "confirmed_at")
  public LocalDateTime confirmedAt;

  @Column(name = "updated_at", nullable = false)
  public LocalDateTime updatedAt = LocalDateTime.now();

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  public List<OrderItemEntity> items = new ArrayList<>();
}
