package com.nttdata.orderflow.kafka;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "orderflow.kafka")
public interface KafkaProperties {
  String topic();

  String bootstrapServers();

  String schemaRegistryUrl();
}
