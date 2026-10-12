# Guia De Pruebas

Secuencia recomendada:

1. Ejecutar `./infra/scripts/start-all.sh`.
2. Abrir `http://localhost:8082/q/swagger-ui`.
3. Generar token en `/auth/token`.
4. Consultar `/api/products` y mostrar productos semilla.
5. Crear un producto nuevo.
6. Crear un cliente.
7. Crear un pedido.
8. Confirmar el pedido.
9. Mostrar que el pedido queda `CONFIRMADO` y con `externalProfileId`.
10. Abrir AKHQ en `http://localhost:8080` y revisar el topic.
11. Consultar `/api/orders/{id}/events` para ver auditoria.

Si se necesita un entorno limpio, ejecutar `./infra/scripts/reset-all.sh`.
