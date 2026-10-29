# Guía para estudiar y defender AutoAndes API

Esta guía orienta la lectura y no reemplaza entender el código. El examen indica que el código asistido por IA debe poder justificarse línea por línea. Practica cada recorrido en IntelliJ y Swagger UI.

## Pregunta 1 — capas y CRUD

- DTO y validación: dto/ServicioRequestDTO.java y dto/VehiculoRequestDTO.java.
- Relación: entity/Vehiculo.java usa @ManyToOne(fetch = LAZY) hacia Cliente.
- Flujo: controller → interfaz e implementación en service → repository → Oracle.
- La conversión de respuestas está en mapper. VehiculoResponseDTO expone clienteId y clienteNombre.

## Pregunta 2 — orden atómica y reporte

- Entrada: dto/OrdenRegistroRequestDTO.java y dto/DetalleOrdenRequestDTO.java.
- Reglas, cálculo de precios/total, número y anulación: service/OrdenServiceImpl.java.
- @Transactional en el servicio agrupa cabecera y detalles; la excepción antes del commit revierte todos los cambios.
- La secuencia seq_numero_orden está en V1.
- El reporte está en repository/ReporteRepository.java: JPQL SUM/GROUP BY, solo REGISTRADA.

## Pregunta 3 — JWT y permisos

- Matriz: security/PermisosPorRol.java.
- BCrypt y cuentas de demostración local: service/AutoAndesDataInitializer.java.
- Login: service/AuthServiceImpl.java. Firma y expiración: security/JwtService.java.
- Filtro de token por petición: security/JwtAuthenticationFilter.java.
- API sin estado, CORS y rutas públicas: config/SecurityConfig.java.
- Cada controlador verifica authorities con @PreAuthorize; no compara nombres de roles.
- 401: RestAuthenticationEntryPoint; 403: RestAccessDeniedHandler y manejador MVC para denegaciones de método.

## Preguntas 4 y 5 — SPA

Lee docs/guia-defensa.md en autoandes-frontend. Confirma nombres de permisos, DTOs y rutas entre los dos proyectos. El total mostrado por Angular es una estimación; el backend lo recalcula con precios de Oracle.
