package com.nttdata.orderflow.customer;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_customers")
public class CustomerEntity extends PanacheEntityBase {
  @Id
  @GeneratedValue
  public UUID id;

  @Column(name = "first_name", nullable = false)
  public String firstName;

  @Column(name = "last_name", nullable = false)
  public String lastName;

  @Column(nullable = false, unique = true)
  public String email;

  @Column(name = "document_number", nullable = false, unique = true)
  public String documentNumber;

  @Column(nullable = false)
  public String status = "ACTIVO";

  @Column(name = "created_at", nullable = false)
  public LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "updated_at", nullable = false)
  public LocalDateTime updatedAt = LocalDateTime.now();
}
