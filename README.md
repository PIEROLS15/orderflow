# OrderFlow

Sistema de gestion de pedidos para tienda online construido con Quarkus. Cubre REST, Hibernate Panache, PostgreSQL, JWT, consumo de API externa, Kafka con Avro + Schema Registry y despliegue completo con Podman.

## Arquitectura

```text
orderflow/
├── orderflow-api       # API REST, JWT, CRUD, API externa y producer Kafka
├── orderflow-consumer  # Consumer Kafka y auditoria
├── infra               # Podman compose, SQL, schemas Avro y scripts
├── bruno               # Coleccion Bruno para probar endpoints
├── docs                # Documentacion de apoyo
└── README.md
```

## Flujo De Negocio

1. Se genera un token JWT.
2. Se consultan productos semilla cargados por `init.sql`.
3. Se puede crear un producto nuevo para demostrar el CRUD.
4. Se registra un cliente.
5. Se crea un pedido en estado `CREADO`.
6. Se confirma el pedido.
7. Durante la confirmacion se consume una API externa para validar perfil.
8. Se descuenta stock y el pedido pasa a `CONFIRMADO`.
9. Se publica un evento Kafka con Avro en `order-confirmed-topic`.
10. `orderflow-consumer` procesa el evento y registra auditoria en `tb_order_event_logs`.

## Requisitos

```text
Podman
Podman Compose
```

No es necesario instalar PostgreSQL, Kafka ni Maven para ejecutar el proyecto con contenedores.

## Levantar Todo

```bash
./infra/scripts/start-all.sh
```

## Detener Sin Perder Datos

```bash
./infra/scripts/stop-all.sh
```

## Reset Completo

Elimina el volumen de PostgreSQL y vuelve a cargar tablas y productos semilla.

```bash
./infra/scripts/reset-all.sh
```

## Accesos

| Servicio | URL |
|---|---|
| API | http://localhost:8082 |
| Swagger UI | http://localhost:8082/q/swagger-ui |
| AKHQ | http://localhost:8080 |
| Schema Registry | http://localhost:8081 |
| PostgreSQL | localhost:15432 |
| Kafka (host) | localhost:19092 |

Credenciales de base de datos:

| Campo | Valor |
|---|---|
| Database | `db_orderflow` |
| User | `postgres` |
| Password | `pgpass321` |

## Probar La API

La coleccion Bruno en `bruno/` contiene todos los requests listos para ejecutar el flujo completo.

Abrir la carpeta `bruno/` desde Bruno y seleccionar el environment `Orderflow`.

Orden recomendado:

```text
01 Auth / 01 Generate Token
02 Products / 01 List Products
02 Products / 03 Create Product
03 Customers / 01 Create Customer
04 Orders / 01 Create Order
04 Orders / 04 Confirm Order
04 Orders / 05 Get Order Events
```

La coleccion guarda automaticamente las variables `token`, `productId`, `customerId` y `orderId` al ejecutar cada request.

Si el cliente ya existe, ejecuta `./infra/scripts/reset-all.sh` o cambia el email/documento en Bruno.

## API Externa

Se consume desde `orderflow-api` durante la confirmacion del pedido.

```text
POST https://api.escuelajs.co/api/v1/auth/login
GET  https://api.escuelajs.co/api/v1/auth/profile
```

## Kafka Avro

| Campo | Valor |
|---|---|
| Topic | `order-confirmed-topic` |
| Schema | `infra/schema-registry/OrderConfirmedEvent.avsc` |
| Serializer | `KafkaAvroSerializer` |
| Deserializer | `KafkaAvroDeserializer` |

## Estados De Negocio

```text
ACTIVO
CREADO
CONFIRMADO
CANCELADO
PROCESADO
PEDIDO_CONFIRMADO
```
