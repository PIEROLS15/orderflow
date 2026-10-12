package com.nttdata.orderflow.order;

import com.nttdata.orderflow.product.ProductEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tb_order_items")
public class OrderItemEntity extends PanacheEntityBase {
  @Id
  @GeneratedValue
  public UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  public OrderEntity order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  public ProductEntity product;

  @Column(nullable = false)
  public int quantity;

  @Column(name = "unit_price", nullable = false)
  public BigDecimal unitPrice;

  @Column(nullable = false)
  public BigDecimal subtotal;
}
