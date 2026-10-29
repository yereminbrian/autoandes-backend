-- Marca las cuentas semilla para que el inicializador las actualice con hashes BCrypt.
UPDATE usuarios
SET password = '!ROTAR_BCRYPT!'
WHERE username IN ('admin', 'asesor', 'jefe', 'inactivo');