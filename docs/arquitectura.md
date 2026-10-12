# Arquitectura

OrderFlow usa dos microservicios Quarkus y componentes de infraestructura levantados con Podman.

```text
Cliente HTTP -> orderflow-api -> PostgreSQL
                       |
                       +-> API externa
                       |
                       +-> Kafka topic order-confirmed-topic
                                      |
                                      v
                             orderflow-consumer -> PostgreSQL
```

## Patrones Arquitectonicos

### orderflow-api

Utiliza **Vertical Slice Architecture** (arquitectura por feature). El codigo esta organizado por dominio de negocio, y cada feature contiene sus propias capas internas:

```text
com.nttdata.orderflow
├── order/          # Feature: Pedidos
│   ├── OrderResource       (capa web REST)
│   ├── OrderService        (logica de negocio)
│   ├── OrderRepository     (acceso a datos con Panache)
│   ├── OrderEntity         (entidad JPA)
│   ├── OrderItemEntity     (entidad JPA)
│   ├── OrderEventLogEntity (entidad JPA)
│   ├── OrderRequest        (DTO entrada)
│   └── OrderResponse       (DTO salida)
├── product/        # Feature: Productos
├── customer/       # Feature: Clientes
├── auth/           # Feature: Autenticacion JWT
├── external/       # Adaptador: API externa (MicroProfile REST Client)
├── kafka/          # Adaptador: Productor Kafka Avro
└── exception/      # Manejo centralizado de errores (ExceptionMapper)
```

Capas dentro de cada feature:

| Capa | Responsabilidad |
|---|---|
| Resource | Recibe peticiones HTTP, valida y delega al service |
| Service | Contiene la logica de negocio y reglas |
| Repository | Acceso a datos con Hibernate Panache |
| Entity | Mapeo a tablas de PostgreSQL |

Separacion de concerns:

| Componente | Proposito |
|---|---|
| DTOs (Request/Response) | Contratos de entrada/salida separados de entidades JPA |
| ExternalAuthClient | Interfaz REST Client para consumo de API externa |
| KafkaProperties | Abstraccion de configuracion Kafka |
| GlobalExceptionMapper | Manejo centralizado de errores con codigos y mensajes |

### orderflow-consumer

Utiliza una arquitectura **simple en capas** (pipeline de ingesta Kafka):

```text
com.nttdata.orderflow.consumer
├── kafka/
│   ├── OrderConfirmedConsumer   (suscripcion a topic)
│   ├── OrderEventProcessor      (transformacion y logica)
│   ├── KafkaConsumerConfig      (configuracion del consumer)
│   └── KafkaProperties          (abstraccion de configuracion)
├── entity/
│   └── OrderEventLogEntity      (entidad JPA)
└── repository/
    └── OrderEventLogRepository  (acceso a datos con Panache)
```

Pipeline:

```text
Consumer Kafka -> Processor -> Repository -> PostgreSQL
```

No expone endpoints REST, solo procesa eventos y persiste auditoria.

## Infraestructura

La base de datos es persistente por volumen Podman y se regenera solo con `reset-all.sh`.
