package com.nttdata.orderflow.consumer.kafka;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import java.util.Properties;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

@ApplicationScoped
public class KafkaConsumerConfig {
  @Inject
  KafkaProperties properties;

  @Produces
  @ApplicationScoped
  Consumer<String, GenericRecord> consumer() {
    var props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.bootstrapServers());
    props.put(ConsumerConfig.GROUP_ID_CONFIG, properties.groupId());
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "10");
    props.put("schema.registry.url", properties.schemaRegistryUrl());
    props.put("specific.avro.reader", "false");
    return new KafkaConsumer<>(props);
  }
}
