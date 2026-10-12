package com.nttdata.orderflow.product;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_products")
public class ProductEntity extends PanacheEntityBase {
  @Id
  @GeneratedValue
  public UUID id;

  @Column(nullable = false)
  public String name;

  public String description;

  @Column(nullable = false)
  public BigDecimal price;

  @Column(nullable = false)
  public int stock;

  @Column(nullable = false)
  public boolean active = true;

  @Column(name = "created_at", nullable = false)
  public LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "updated_at", nullable = false)
  public LocalDateTime updatedAt = LocalDateTime.now();
}
