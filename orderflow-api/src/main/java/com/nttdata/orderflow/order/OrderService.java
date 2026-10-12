package com.nttdata.orderflow.order;

import com.nttdata.orderflow.customer.CustomerRepository;
import com.nttdata.orderflow.exception.BusinessException;
import com.nttdata.orderflow.external.ExternalProfileService;
import com.nttdata.orderflow.kafka.OrderConfirmedPublisher;
import com.nttdata.orderflow.product.ProductEntity;
import com.nttdata.orderflow.product.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrderService {
  @Inject
  OrderRepository orderRepository;

  @Inject
  OrderEventLogRepository eventLogRepository;

  @Inject
  CustomerRepository customerRepository;

  @Inject
  ProductRepository productRepository;

  @Inject
  ExternalProfileService externalProfileService;

  @Inject
  OrderConfirmedPublisher publisher;

  @Transactional
  public OrderResponse create(OrderRequest request) {
    var customer = customerRepository.findByIdOptional(request.customerId())
        .orElseThrow(() -> new BusinessException("CLIENTE_NO_ENCONTRADO", "Cliente no encontrado", Response.Status.NOT_FOUND));

    var order = new OrderEntity();
    order.customer = customer;
    order.status = OrderStatus.CREADO;

    for (var itemRequest : request.items()) {
      var product = findActiveProduct(itemRequest.productId());
      var item = new OrderItemEntity();
      item.order = order;
      item.product = product;
      item.quantity = itemRequest.quantity();
      item.unitPrice = product.price;
      item.subtotal = product.price.multiply(BigDecimal.valueOf(item.quantity));
      order.totalAmount = order.totalAmount.add(item.subtotal);
      order.items.add(item);
    }

    orderRepository.persist(order);
    return OrderResponse.from(order);
  }

  public List<OrderResponse> list() {
    return orderRepository.listAll().stream().map(OrderResponse::from).toList();
  }

  public OrderResponse get(UUID id) {
    return OrderResponse.from(findOrder(id));
  }

  public List<OrderResponse> byCustomer(UUID customerId) {
    return orderRepository.findByCustomer(customerId).stream().map(OrderResponse::from).toList();
  }

  public List<OrderResponse> byStatus(String status) {
    return orderRepository.findByStatus(status).stream().map(OrderResponse::from).toList();
  }

  @Transactional
  public OrderResponse update(UUID id, OrderRequest request) {
    var order = findOrder(id);
    ensureCreated(order, "Solo los pedidos CREADOS pueden actualizarse");
    order.items.clear();
    order.totalAmount = BigDecimal.ZERO;
    order.updatedAt = LocalDateTime.now();

    for (var itemRequest : request.items()) {
      var product = findActiveProduct(itemRequest.productId());
      var item = new OrderItemEntity();
      item.order = order;
      item.product = product;
      item.quantity = itemRequest.quantity();
      item.unitPrice = product.price;
      item.subtotal = product.price.multiply(BigDecimal.valueOf(item.quantity));
      order.totalAmount = order.totalAmount.add(item.subtotal);
      order.items.add(item);
    }
    return OrderResponse.from(order);
  }

  @Transactional
  public void delete(UUID id) {
    var order = findOrder(id);
    ensureCreated(order, "Solo los pedidos CREADOS pueden eliminarse");
    orderRepository.delete(order);
  }

  @Transactional
  public OrderResponse confirm(UUID id) {
    var order = findOrder(id);
    ensureCreated(order, "Solo los pedidos CREADOS pueden confirmarse");
    validateStock(order);

    var profile = externalProfileService.validateProfile();
    order.externalProfileId = profile.id() == null ? null : profile.id().toString();
    for (var item : order.items) {
      item.product.stock -= item.quantity;
      item.product.updatedAt = LocalDateTime.now();
    }
    order.status = OrderStatus.CONFIRMADO;
    order.confirmedAt = LocalDateTime.now();
    order.updatedAt = LocalDateTime.now();

    publisher.publish(order);
    return OrderResponse.from(order);
  }

  @Transactional
  public OrderResponse cancel(UUID id) {
    var order = findOrder(id);
    ensureCreated(order, "Solo los pedidos CREADOS pueden cancelarse");
    order.status = OrderStatus.CANCELADO;
    order.updatedAt = LocalDateTime.now();
    return OrderResponse.from(order);
  }

  public List<OrderEventLogResponse> logs(UUID orderId) {
    findOrder(orderId);
    return eventLogRepository.findByOrderId(orderId).stream().map(OrderEventLogResponse::from).toList();
  }

  private OrderEntity findOrder(UUID id) {
    return orderRepository.findByIdOptional(id)
        .orElseThrow(() -> new BusinessException("PEDIDO_NO_ENCONTRADO", "Pedido no encontrado", Response.Status.NOT_FOUND));
  }

  private ProductEntity findActiveProduct(UUID id) {
    var product = productRepository.findByIdOptional(id)
        .orElseThrow(() -> new BusinessException("PRODUCTO_NO_ENCONTRADO", "Producto no encontrado", Response.Status.NOT_FOUND));
    if (!product.active) {
      throw new BusinessException("PRODUCTO_INACTIVO", "El producto esta inactivo", Response.Status.CONFLICT);
    }
    return product;
  }

  private void validateStock(OrderEntity order) {
    for (var item : order.items) {
      if (item.product.stock < item.quantity) {
        throw new BusinessException("STOCK_INSUFICIENTE", "Stock insuficiente para el producto " + item.product.name,
            Response.Status.CONFLICT);
      }
    }
  }

  private void ensureCreated(OrderEntity order, String message) {
    if (!OrderStatus.CREADO.equals(order.status)) {
      throw new BusinessException("ESTADO_PEDIDO_INVALIDO", message, Response.Status.CONFLICT);
    }
  }
}
