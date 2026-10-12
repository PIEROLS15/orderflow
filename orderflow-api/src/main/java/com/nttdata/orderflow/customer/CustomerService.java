package com.nttdata.orderflow.customer;

import com.nttdata.orderflow.exception.BusinessException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CustomerService {
  @Inject
  CustomerRepository repository;

  @Transactional
  public CustomerResponse create(CustomerRequest request) {
    repository.findByEmail(request.email()).ifPresent(existing -> {
      throw new BusinessException("CLIENTE_EMAIL_EXISTE", "El email del cliente ya existe", Response.Status.CONFLICT);
    });
    var entity = new CustomerEntity();
    entity.firstName = request.firstName();
    entity.lastName = request.lastName();
    entity.email = request.email();
    entity.documentNumber = request.documentNumber();
    repository.persist(entity);
    return CustomerResponse.from(entity);
  }

  public List<CustomerResponse> list() {
    return repository.listAll().stream().map(CustomerResponse::from).toList();
  }

  public CustomerResponse get(UUID id) {
    return CustomerResponse.from(findEntity(id));
  }

  public CustomerResponse searchByEmail(String email) {
    return repository.findByEmail(email).map(CustomerResponse::from)
        .orElseThrow(() -> new BusinessException("CLIENTE_NO_ENCONTRADO", "Cliente no encontrado", Response.Status.NOT_FOUND));
  }

  @Transactional
  public CustomerResponse update(UUID id, CustomerRequest request) {
    var entity = findEntity(id);
    entity.firstName = request.firstName();
    entity.lastName = request.lastName();
    entity.email = request.email();
    entity.documentNumber = request.documentNumber();
    entity.updatedAt = LocalDateTime.now();
    return CustomerResponse.from(entity);
  }

  @Transactional
  public void delete(UUID id) {
    if (!repository.deleteById(id)) {
      throw new BusinessException("CLIENTE_NO_ENCONTRADO", "Cliente no encontrado", Response.Status.NOT_FOUND);
    }
  }

  CustomerEntity findEntity(UUID id) {
    return repository.findByIdOptional(id)
        .orElseThrow(() -> new BusinessException("CLIENTE_NO_ENCONTRADO", "Cliente no encontrado", Response.Status.NOT_FOUND));
  }
}
