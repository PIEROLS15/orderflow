package com.nttdata.orderflow.consumer.kafka;

import com.nttdata.orderflow.consumer.entity.OrderEventLogEntity;
import com.nttdata.orderflow.consumer.repository.OrderEventLogRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;
import org.apache.avro.generic.GenericRecord;

@ApplicationScoped
public class OrderEventProcessor {
  @Inject
  OrderEventLogRepository repository;

  @Transactional
  public void process(GenericRecord record) {
    var log = new OrderEventLogEntity();
    log.orderId = UUID.fromString(record.get("orderId").toString());
    log.eventType = "PEDIDO_CONFIRMADO";
    log.status = "PROCESADO";
    log.message = "Evento de pedido confirmado procesado para el cliente " + record.get("customerEmail");
    log.processedAt = LocalDateTime.now();
    repository.persist(log);
  }
}
