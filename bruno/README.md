# Coleccion Bruno - OrderFlow

Abrir esta carpeta desde Bruno:
```text
\bruno
```

Seleccionar el environment:

```text
Orderflow
```

## Orden Recomendado

1. `01 Auth / 01 Generate Token`
2. `02 Products / 01 List Products`
3. `02 Products / 03 Create Product`
4. `03 Customers / 01 Create Customer`
5. `04 Orders / 01 Create Order`
6. `04 Orders / 04 Confirm Order`
7. Esperar unos segundos a que el consumer procese Kafka.
8. `04 Orders / 05 Get Order Events`

## Variables Guardadas Automaticamente

La coleccion guarda estas variables en el environment `Orderflow`:

```text
token
productId
customerId
orderId
```

Se guardan al ejecutar:

```text
Generate Token -> token
Create Product -> productId
Create Customer -> customerId
Create Order -> orderId
```

## Notas

No ejecutes `Delete Product` antes de crear el pedido si quieres usar el producto creado en el flujo principal.

Si repites el flujo completo y el cliente ya existe, ejecuta primero:

```bash
./infra/scripts/reset-all.sh
```

Tambien puedes cambiar el email y documentNumber del request `Create Customer`.
