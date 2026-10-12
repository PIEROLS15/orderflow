package com.nttdata.orderflow.kafka;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import java.util.Properties;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

@ApplicationScoped
public class KafkaProducerConfig {
  @Inject
  KafkaProperties properties;

  @Produces
  @ApplicationScoped
  Producer<String, GenericRecord> producer() {
    var props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.bootstrapServers());
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
    props.put("schema.registry.url", properties.schemaRegistryUrl());
    props.put("auto.register.schemas", "true");
    return new KafkaProducer<>(props);
  }
}
