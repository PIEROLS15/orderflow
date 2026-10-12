package com.nttdata.orderflow.consumer.repository;

import com.nttdata.orderflow.consumer.entity.OrderEventLogEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class OrderEventLogRepository implements PanacheRepositoryBase<OrderEventLogEntity, UUID> {
}
