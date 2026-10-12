package com.nttdata.orderflow.kafka;

import com.nttdata.orderflow.order.OrderEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jboss.logging.Logger;

@ApplicationScoped
public class OrderConfirmedPublisher {
  private static final Logger LOG = Logger.getLogger(OrderConfirmedPublisher.class);
  private final Schema schema;

  @Inject
  KafkaProperties properties;

  @Inject
  Producer<String, GenericRecord> producer;

  public OrderConfirmedPublisher() {
    try (var input = Thread.currentThread().getContextClassLoader().getResourceAsStream("avro/OrderConfirmedEvent.avsc")) {
      this.schema = new Schema.Parser().parse(input);
    } catch (Exception exception) {
      throw new IllegalStateException("Cannot load OrderConfirmedEvent schema", exception);
    }
  }

  public void publish(OrderEntity order) {
    GenericRecord record = new GenericData.Record(schema);
    record.put("orderId", order.id.toString());
    record.put("customerId", order.customer.id.toString());
    record.put("customerEmail", order.customer.email);
    record.put("totalAmount", order.totalAmount.doubleValue());
    record.put("status", order.status);
    record.put("confirmedAt", order.confirmedAt.toString());

    producer.send(new ProducerRecord<>(properties.topic(), order.id.toString(), record), (metadata, error) -> {
      if (error != null) {
        LOG.errorf(error, "Error publishing order event %s", order.id);
      } else {
        LOG.infof("Order event published topic=%s partition=%d offset=%d", metadata.topic(), metadata.partition(), metadata.offset());
      }
    });
  }
}
