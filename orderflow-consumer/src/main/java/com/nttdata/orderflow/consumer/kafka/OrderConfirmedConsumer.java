package com.nttdata.orderflow.consumer.kafka;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.Consumer;
import org.jboss.logging.Logger;

@ApplicationScoped
public class OrderConfirmedConsumer {
  private static final Logger LOG = Logger.getLogger(OrderConfirmedConsumer.class);

  @Inject
  KafkaProperties properties;

  @Inject
  Consumer<String, GenericRecord> consumer;

  @Inject
  OrderEventProcessor processor;

  private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
  private volatile boolean running = true;

  void start(@Observes StartupEvent event) {
    consumer.subscribe(Collections.singleton(properties.topic()));
    executor.submit(() -> {
      LOG.infof("Consuming topic %s", properties.topic());
      while (running) {
        try {
          var records = consumer.poll(Duration.ofSeconds(1));
          for (var record : records) {
            processor.process(record.value());
            LOG.infof("Evento de pedido procesado key=%s offset=%d", record.key(), record.offset());
          }
          if (!records.isEmpty()) {
            consumer.commitSync();
          }
        } catch (Exception exception) {
          LOG.error("Error al consumir evento de pedido", exception);
        }
      }
    });
  }

  void stop(@Observes ShutdownEvent event) {
    running = false;
    consumer.wakeup();
    executor.shutdown();
  }
}
