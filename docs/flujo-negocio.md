# Flujo De Negocio

El proceso representa la gestion de pedidos de una tienda online.

1. El usuario obtiene un token.
2. Consulta productos disponibles.
3. Registra un cliente.
4. Crea un pedido con productos y cantidades.
5. El pedido queda en estado `CREADO`.
6. Al confirmar, se valida stock.
7. Se consume una API externa para validar perfil.
8. El pedido pasa a `CONFIRMADO`.
9. Se descuenta stock.
10. Se publica un evento Kafka Avro.
11. El consumer registra auditoria del evento.

Estados permitidos:

```text
CREADO -> CONFIRMADO
CREADO -> CANCELADO
```

Pedidos `CONFIRMADO` o `CANCELADO` no se editan ni eliminan.
