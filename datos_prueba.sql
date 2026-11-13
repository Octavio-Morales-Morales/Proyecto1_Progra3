ALTER TABLE `TURNO`
  DROP INDEX `codigo_turno`,
  ADD UNIQUE KEY `uq_turno_codigo_fecha` (`codigo_turno`, `generacion`);

INSERT INTO `SERVICIO` (`servicio_id`, `codigo`, `nombre`, `descripcion`, `activo`) VALUES
(1, 'A', 'Información general', 'Consultas e información', 1),
(2, 'B', 'Trámites', 'Trámites y solicitudes', 1),
(3, 'C', 'Pagos', 'Pagos y cobros', 1),
(4, 'D', 'Atención preferencial', 'Adultos mayores, embarazadas y personas con discapacidad', 1);

INSERT INTO `VENTANILLA` (`ventanilla_id`, `numero`, `estado`, `ubicacion`, `activo`) VALUES
(1, 1, 'DISPONIBLE', 'Planta baja', 1),
(2, 2, 'DISPONIBLE', 'Planta baja', 1),
(3, 3, 'DISPONIBLE', 'Planta baja', 1),
(4, 4, 'DISPONIBLE', 'Planta baja', 1);

INSERT INTO `VENTANILLA_SERVICIO` (`ventanilla_id`, `servicio_id`, `activo`) VALUES
(1, 1, 1), (1, 2, 1),
(2, 1, 1), (2, 3, 1),
(3, 2, 1), (3, 3, 1),
(4, 4, 1), (4, 1, 1);

INSERT INTO `USUARIO` (`usuario_id`, `nombre`, `usuario`, `password_hash`, `rol_id`, `activo`) VALUES
(1, 'Administrador', 'admin',     '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 1, 1),
(2, 'Ana Rodríguez', 'operador1', '13a93f9a5502e71eed80a806f87bc4424e03e6b0d90acd513ea9d561c853b738', 2, 1),
(3, 'Luis Mora',     'operador2', '13a93f9a5502e71eed80a806f87bc4424e03e6b0d90acd513ea9d561c853b738', 2, 1);

INSERT INTO `FUNCIONARIO` (`funcionario_id`, `usuario_id`, `nombre_completo`, `identificacion`, `activo`) VALUES
(1, 2, 'Ana Rodríguez Solís', '1-1111-1111', 1),
(2, 3, 'Luis Mora Vargas',    '2-2222-2222', 1);
