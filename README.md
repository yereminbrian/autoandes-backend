# AutoAndes API

Backend REST del examen práctico LP2: servicios, vehículos, clientes, órdenes y reporte de ingresos.

## Requisitos

- JDK 21 (Spring Boot 4 requiere Java 17 o posterior).
- Maven 3.6.3 o posterior. IntelliJ IDEA incluye Maven y permite ejecutar spring-boot:run.
- Una instancia Oracle accesible desde la máquina que ejecuta la aplicación.

## Oracle

El nombre solicitado para el esquema/usuario es AUTOANDES. Flyway crea las tablas y los datos semilla al arrancar. El URL predeterminado es jdbc:oracle:thin:@//localhost:1522/FREEPDB1; reemplázalo por el host, puerto y SERVICE_NAME que exponga tu contenedor.

Configura estas variables en PowerShell antes de ejecutar:

$env:ORACLE_URL = "jdbc:oracle:thin:@//localhost:1522/FREEPDB1"
$env:ORACLE_USERNAME = "AUTOANDES"
$env:ORACLE_PASSWORD = "<contraseña configurada en Oracle>"
$env:JWT_SECRET = "<secreto local de al menos 32 bytes>"
mvn spring-boot:run

Desde IntelliJ, define las mismas variables en la Run Configuration. No guardes contraseña ni secreto en Git. JPA valida el esquema con ddl-auto: validate; la única fuente de creación/carga es src/main/resources/db/migration/V1__esquema_y_semilla.sql.

Aún falta ejecutar la migración contra tu instancia Oracle. Crear el usuario/esquema es responsabilidad del entorno local. Si el contenedor expone otro servicio, actualiza ORACLE_URL.

## Usuarios de demostración

En el primer arranque, el backend reemplaza los marcadores de las cuentas semilla por hashes BCrypt. Las claves locales siguen la regla usuario en minúsculas + 123:

| Usuario | Clave local | Estado | Rol |
|---|---|---|---|
| admin | admin123 | Activo | ADMIN |
| asesor | asesor123 | Activo | ASESOR |
| jefe | jefe123 | Activo | JEFE_TALLER |
| inactivo | inactivo123 | Inactivo | ASESOR |

Son credenciales únicamente para la base semilla local. Cámbialas antes de un despliegue.

## Rutas principales

Todas las rutas usan /api/v1; salvo POST /auth/login, requieren JWT Bearer.

- POST /auth/login y GET /auth/me
- GET /clientes
- CRUD /servicios; PATCH /servicios/{id}/estado
- CRUD /vehiculos?clienteId=
- POST /ordenes, GET /ordenes?placa&estado&desde&hasta, GET /ordenes/{id}, PATCH /ordenes/{id}/anular
- GET /reportes/ingresos-por-servicio?desde=YYYY-MM-DD&hasta=YYYY-MM-DD

Swagger UI: http://localhost:8080/swagger-ui.html.

## Reglas del caso

- DTOs separan solicitudes y respuestas; la respuesta de vehículo incluye clienteId y clienteNombre.
- Permisos se convierten en authorities en PermisosPorRol. ADMIN obtiene todos; los demás roles reciben la unión de su matriz.
- Los controladores aplican permisos con @PreAuthorize. El backend es la frontera real de autorización.
- OrdenServiceImpl valida detalles, consulta precios actuales, calcula subtotales y total y guarda cabecera y detalles en una transacción. Un error provoca rollback.
- El kilometraje se compara con la última orden REGISTRADA del vehículo.
- Para dar de baja un servicio usado se emplea PATCH; DELETE devuelve 409. Un vehículo con órdenes no se elimina.
- El reporte se calcula con JPQL agregado y excluye órdenes ANULADAS.
- Errores 400/401/403/404/409/500 usan ErrorResponseDTO; los errores de validación incluyen campos.

## Defensa

Consulta docs/guia-defensa.md para ubicar cada flujo. El examen exige explicar cada línea de código asistido por IA. Repasa y ejecuta cada parte con tu instancia Oracle antes de la defensa. No se afirma que el backend ya se haya probado contra Oracle.
