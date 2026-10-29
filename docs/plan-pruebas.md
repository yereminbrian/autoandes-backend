# Plan de pruebas del examen AutoAndes

Este plan traduce a verificaciones ejecutables la evidencia que pide el PDF. El PDF prioriza pruebas de integración y demostración directa con Swagger/Postman, Oracle y la SPA; no exige una cantidad determinada de pruebas unitarias JUnit.

## Preparación

- [ ] Docker Oracle activo en `localhost:1522`, servicio `FREEPDB1`, esquema `AUTOANDES`.
- [ ] Iniciar el backend y confirmar Flyway en versión `2`, Hibernate inicializado y Tomcat en `8080`.
- [ ] Después de aplicar V2, usar `admin/admin123`, `asesor/asesor123` y `jefe/jefe123`. El usuario `inactivo/inactivo123` debe ser rechazado.
- [ ] Iniciar Angular con `npm start` en `http://localhost:4200`.
- [ ] Importar `postman/AutoAndes.postman_collection.json`, ejecutar los tres logins y guardar los tokens.

> En el último registro compartido, Flyway estaba en versión 1. Si no se reinició después de añadir V2, hacerlo antes de probar las nuevas contraseñas.

## P1 — CRUD de Servicios, Vehículos y Clientes

| Caso | Acción | Resultado esperado |
|---|---|---|
| P1-S1 | Listar servicios y consultar un servicio existente | `200`; DTO con campos del servicio |
| P1-S2 | Crear, consultar y editar un servicio | `201`, `200`, `204`; cambios persistidos en Oracle |
| P1-S3 | Crear servicio con código/nombre/precio/duración inválidos | `400`; formato común y errores en `campos` |
| P1-S4 | Crear código duplicado y consultar ID inexistente | `409` y `404` |
| P1-S5 | Dar de baja un servicio sin uso; eliminar uno usado por una orden | `204`; el usado responde `409`; probar PATCH de estado `200` |
| P1-V1 | Listar vehículos, filtrar por cliente, consultar uno | `200`; incluye nombre e ID del cliente |
| P1-V2 | Crear, consultar y editar un vehículo con cliente existente | `201`, `200`, `204` |
| P1-V3 | Placa inválida, cliente inexistente y placa duplicada | `400`, `404`, `409` |
| P1-V4 | Eliminar vehículo libre y vehículo con orden registrada | `204` y `409` |
| P1-C1 | Listar clientes con token y sin token | `200` y `401`; no existe CRUD de clientes |

## P2 — Órdenes, transacción, filtros y reporte

| Caso | Acción | Resultado esperado |
|---|---|---|
| P2-O1 | Registrar orden con vehículo, kilometraje y varios servicios activos | `201`; precios salen de BD; subtotal y total correctos; cabecera y detalles persistidos |
| P2-O2 | Enviar tres detalles válidos salvo el tercero, que usa servicio inactivo | `409`; verificar en Oracle que no quedó cabecera ni detalle de esa petición (rollback total) |
| P2-O3 | Usar cantidad fuera de 1–5, servicio repetido o número de detalles fuera de 1–10 | `400`; respuesta común |
| P2-O4 | Registrar con kilometraje menor al de la última orden registrada | `409`; mensaje indica el kilometraje mínimo |
| P2-O5 | Consultar lista con filtros combinados, por separado y sin resultados | `200`; orden descendente y lista vacía para búsqueda sin coincidencias |
| P2-O6 | Consultar detalle existente e inexistente | `200` y `404` |
| P2-O7 | Anular orden registrada hoy con motivo de 10–200 caracteres; probar motivo inválido y segunda anulación | `200`; `400` por motivo inválido y `409` si ya no es anulable |
| P2-R1 | Consultar ingresos por servicio y contrastar con órdenes | `200`; agrupado por servicio, importe descendente y sin órdenes anuladas |

Para demostrar P2-O1 y P2-O2 en DBeaver:

```sql
SELECT * FROM orden_servicio ORDER BY id DESC;
SELECT * FROM detalle_orden ORDER BY id DESC;
```

Anotar el número de orden del caso fallido y confirmar que no aparece en ninguna tabla. Para el reporte, comparar los subtotales de detalles de órdenes registradas y comprobar que las anuladas no aportan importe.

## P3 — Login, JWT y permisos en el backend

| Caso | Acción | Resultado esperado |
|---|---|---|
| P3-A1 | Login de ADMIN, ASESOR y JEFE_TALLER | `200`; Bearer, vencimiento 3600, nombre, roles y permisos; no devuelve contraseña |
| P3-A2 | Usuario inexistente, contraseña errónea y usuario inactivo | `401`; mismo mensaje genérico |
| P3-A3 | Consultar `/auth/me` con token válido | `200`; usuario y permisos del token |
| P3-A4 | Llamar una ruta protegida sin token y con token alterado | `401` con formato común |
| P3-A5 | ASESOR intenta anular una orden y ver reporte; JEFE_TALLER intenta registrar orden | `403` con formato común; tokens válidos |
| P3-A6 | ASESOR intenta registrar orden y JEFE_TALLER consulta/anula según permisos | Permitir lo autorizado y rechazar lo demás por permiso, no por nombre de rol |

## P4 — CRUD y errores en la SPA

- [ ] Registrar y editar un servicio desde el mismo formulario; comprobar lista, búsqueda y baja confirmada.
- [ ] Registrar y editar un vehículo, seleccionando cliente desde la lista cargada por API; filtrar por cliente.
- [ ] Provocar validación `400` y comprobar mensajes bajo los campos.
- [ ] Registrar una placa repetida y comprobar el `409` del backend en pantalla; la lista debe seguir usable.
- [ ] Comprobar carga, lista vacía, error de API, detalle de `404` y baja de vehículo con orden (`409`).

## P5 — Órdenes y seguridad en la SPA

- [ ] Iniciar como ASESOR y registrar una orden; observar número, detalles y total.
- [ ] Añadir/quitar líneas; comprobar servicio único, cantidad 1–5 y recálculo de subtotales/total.
- [ ] Consultar y filtrar órdenes; abrir detalle y anular una con motivo válido.
- [ ] Iniciar como JEFE_TALLER: «Nueva orden» no aparece y `/ordenes/nueva` escrita directamente muestra «Acceso denegado».
- [ ] Alterar el token en `sessionStorage`: la SPA debe volver a login.
- [ ] Confirmar que un `401` cierra sesión y un `403` conserva la sesión.

## Entregables/evidencia del PDF

- [ ] Postman exportado con ejemplos de `200/201`, `400`, `401`, `403`, `404` y `409`.
- [ ] Capturas del login, CRUD, registro de orden cabecera–detalle, reporte, rechazo `403` y página de acceso denegado.
- [ ] Mostrar en DBeaver la transacción exitosa y la ausencia de filas para la orden rechazada.
- [ ] README de ambos repositorios con comandos de ejecución, usuarios de prueba y contrato API.

## Cobertura actual de la colección Postman

La colección actual cubre login y tokens de los tres roles, lista/alta/validación/conflicto de servicios, lista/404/sin token/403 de vehículos-reporte, alta de orden, kilometraje inválido, consulta y reporte. Aún falta convertir varias demostraciones de este plan en solicitudes de colección: CRUD completo de vehículos/servicios, rollback por detalle inactivo, anulación, token alterado, login fallido/inactivo y JEFE_TALLER intentando registrar una orden.