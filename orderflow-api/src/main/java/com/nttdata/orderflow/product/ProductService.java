package com.nttdata.orderflow.product;

import com.nttdata.orderflow.exception.BusinessException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ProductService {
  @Inject
  ProductRepository repository;

  @Transactional
  public ProductResponse create(ProductRequest request) {
    var entity = new ProductEntity();
    fill(entity, request);
    repository.persist(entity);
    return ProductResponse.from(entity);
  }

  public List<ProductResponse> list() {
    return repository.listAll().stream().map(ProductResponse::from).toList();
  }

  public List<ProductResponse> listActive() {
    return repository.listActive().stream().map(ProductResponse::from).toList();
  }

  public ProductResponse get(UUID id) {
    return ProductResponse.from(findEntity(id));
  }

  @Transactional
  public ProductResponse update(UUID id, ProductRequest request) {
    var entity = findEntity(id);
    fill(entity, request);
    entity.updatedAt = LocalDateTime.now();
    return ProductResponse.from(entity);
  }

  @Transactional
  public void delete(UUID id) {
    if (!repository.deleteById(id)) {
      throw new BusinessException("PRODUCTO_NO_ENCONTRADO", "Producto no encontrado", Response.Status.NOT_FOUND);
    }
  }

  ProductEntity findEntity(UUID id) {
    return repository.findByIdOptional(id)
        .orElseThrow(() -> new BusinessException("PRODUCTO_NO_ENCONTRADO", "Producto no encontrado", Response.Status.NOT_FOUND));
  }

  private void fill(ProductEntity entity, ProductRequest request) {
    entity.name = request.name();
    entity.description = request.description();
    entity.price = request.price();
    entity.stock = request.stock();
    entity.active = request.active();
  }
}
