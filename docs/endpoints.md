# Endpoints

Auth:

```http
POST /auth/token
```

Customers:

```http
POST /api/customers
GET /api/customers
GET /api/customers/{id}
PUT /api/customers/{id}
DELETE /api/customers/{id}
GET /api/customers/search?email=
```

Products:

```http
POST /api/products
GET /api/products
GET /api/products/{id}
PUT /api/products/{id}
DELETE /api/products/{id}
GET /api/products/active
```

Orders:

```http
POST /api/orders
GET /api/orders
GET /api/orders/{id}
PUT /api/orders/{id}
DELETE /api/orders/{id}
POST /api/orders/{id}/confirm
POST /api/orders/{id}/cancel
GET /api/orders/customer/{customerId}
GET /api/orders/status/{status}
GET /api/orders/{id}/events
```

Estados validos para buscar pedidos por estado:

```text
CREADO
CONFIRMADO
CANCELADO
```
