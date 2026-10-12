package com.nttdata.orderflow.customer;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CustomerRepository implements PanacheRepositoryBase<CustomerEntity, UUID> {
  public Optional<CustomerEntity> findByEmail(String email) {
    return find("email", email).firstResultOptional();
  }
}
