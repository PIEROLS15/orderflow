package com.nttdata.orderflow.consumer.kafka;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "orderflow.kafka")
public interface KafkaProperties {
  String topic();

  String bootstrapServers();

  String schemaRegistryUrl();

  String groupId();
}
