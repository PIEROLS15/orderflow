package com.nttdata.orderflow.product;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ProductRepository implements PanacheRepositoryBase<ProductEntity, UUID> {
  public List<ProductEntity> listActive() {
    return list("active", true);
  }
}
