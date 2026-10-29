-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 10.123.0.165:3306
-- Tiempo de generación: 28-09-2026 a las 19:47:14
-- Versión del servidor: 8.4.7
-- Versión de PHP: 8.2.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `gamabasis_p3g1`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ATENCION`
--

CREATE TABLE `ATENCION` (
  `atencion_id` int NOT NULL,
  `turno_id` int NOT NULL,
  `ventanilla_id` int NOT NULL,
  `funcionario_id` int NOT NULL,
  `hora_llamado` datetime DEFAULT NULL,
  `hora_inicio` datetime DEFAULT NULL,
  `hora_finalizacion` datetime DEFAULT NULL,
  `observaciones` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ESTADO_TURNO`
--

CREATE TABLE `ESTADO_TURNO` (
  `estado_id` int NOT NULL,
  `nombre` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `ESTADO_TURNO`
--

INSERT INTO `ESTADO_TURNO` (`estado_id`, `nombre`) VALUES
(5, 'CANCELADO'),
(3, 'EN_ATENCION'),
(1, 'EN_ESPERA'),
(4, 'FINALIZADO'),
(2, 'LLAMADO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `FUNCIONARIO`
--

CREATE TABLE `FUNCIONARIO` (
  `funcionario_id` int NOT NULL,
  `usuario_id` int NOT NULL,
  `nombre_completo` varchar(150) NOT NULL,
  `identificacion` varchar(30) NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ROL`
--

CREATE TABLE `ROL` (
  `rol_id` int NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `ROL`
--

INSERT INTO `ROL` (`rol_id`, `nombre`, `descripcion`) VALUES
(1, 'ADMINISTRADOR', 'Administrador del sistema'),
(2, 'OPERADOR', 'Operador encargado de atender turnos');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `SERVICIO`
--

CREATE TABLE `SERVICIO` (
  `servicio_id` int NOT NULL,
  `codigo` varchar(50) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `TURNO`
--

CREATE TABLE `TURNO` (
  `turno_id` int NOT NULL,
  `codigo_turno` varchar(20) NOT NULL,
  `servicio_id` int NOT NULL,
  `generacion` date NOT NULL,
  `hora_generacion` time NOT NULL,
  `estado_id` int NOT NULL,
  `prioridad` int NOT NULL DEFAULT '0',
  `ventanilla_id` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `TURNOTRABAJO`
--

CREATE TABLE `TURNOTRABAJO` (
  `TURNOTRABAJO_Id` int NOT NULL,
  `Turno_entrada` datetime NOT NULL,
  `Turno_Salida` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `TURNO_TTRABAJO`
--

CREATE TABLE `TURNO_TTRABAJO` (
  `TURNO_TRABAJO_Id` int NOT NULL,
  `TURNO_Id` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `USUARIO`
--

CREATE TABLE `USUARIO` (
  `usuario_id` int NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `usuario` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `rol_id` int NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `VENTANILLA`
--

CREATE TABLE `VENTANILLA` (
  `ventanilla_id` int NOT NULL,
  `numero` int NOT NULL,
  `estado` varchar(30) NOT NULL,
  `ubicacion` varchar(100) DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `VENTANILLA_SERVICIO`
--

CREATE TABLE `VENTANILLA_SERVICIO` (
  `ventanilla_id` int NOT NULL,
  `servicio_id` int NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `ATENCION`
--
ALTER TABLE `ATENCION`
  ADD PRIMARY KEY (`atencion_id`),
  ADD KEY `fk_atencion_turno` (`turno_id`),
  ADD KEY `fk_atencion_ventanilla` (`ventanilla_id`),
  ADD KEY `fk_atencion_funcionario` (`funcionario_id`);

--
-- Indices de la tabla `ESTADO_TURNO`
--
ALTER TABLE `ESTADO_TURNO`
  ADD PRIMARY KEY (`estado_id`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `FUNCIONARIO`
--
ALTER TABLE `FUNCIONARIO`
  ADD PRIMARY KEY (`funcionario_id`),
  ADD UNIQUE KEY `identificacion` (`identificacion`),
  ADD KEY `fk_funcionario_usuario` (`usuario_id`);

--
-- Indices de la tabla `ROL`
--
ALTER TABLE `ROL`
  ADD PRIMARY KEY (`rol_id`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `SERVICIO`
--
ALTER TABLE `SERVICIO`
  ADD PRIMARY KEY (`servicio_id`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `TURNO`
--
ALTER TABLE `TURNO`
  ADD PRIMARY KEY (`turno_id`),
  ADD UNIQUE KEY `codigo_turno` (`codigo_turno`),
  ADD KEY `fk_turno_servicio` (`servicio_id`),
  ADD KEY `fk_turno_estado` (`estado_id`),
  ADD KEY `fk_turno_ventanilla` (`ventanilla_id`);

--
-- Indices de la tabla `TURNOTRABAJO`
--
ALTER TABLE `TURNOTRABAJO`
  ADD PRIMARY KEY (`TURNOTRABAJO_Id`);

--
-- Indices de la tabla `TURNO_TTRABAJO`
--
ALTER TABLE `TURNO_TTRABAJO`
  ADD UNIQUE KEY `TURNO_Id` (`TURNO_Id`) USING BTREE,
  ADD KEY `TURNO_TRABAJO_Id` (`TURNO_TRABAJO_Id`) USING BTREE;

--
-- Indices de la tabla `USUARIO`
--
ALTER TABLE `USUARIO`
  ADD PRIMARY KEY (`usuario_id`),
  ADD UNIQUE KEY `usuario` (`usuario`),
  ADD KEY `fk_usuario_rol` (`rol_id`);

--
-- Indices de la tabla `VENTANILLA`
--
ALTER TABLE `VENTANILLA`
  ADD PRIMARY KEY (`ventanilla_id`),
  ADD UNIQUE KEY `numero` (`numero`);

--
-- Indices de la tabla `VENTANILLA_SERVICIO`
--
ALTER TABLE `VENTANILLA_SERVICIO`
  ADD PRIMARY KEY (`ventanilla_id`,`servicio_id`),
  ADD KEY `fk_vs_servicio` (`servicio_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `ATENCION`
--
ALTER TABLE `ATENCION`
  MODIFY `atencion_id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `ESTADO_TURNO`
--
ALTER TABLE `ESTADO_TURNO`
  MODIFY `estado_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `FUNCIONARIO`
--
ALTER TABLE `FUNCIONARIO`
  MODIFY `funcionario_id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `ROL`
--
ALTER TABLE `ROL`
  MODIFY `rol_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `SERVICIO`
--
ALTER TABLE `SERVICIO`
  MODIFY `servicio_id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `TURNO`
--
ALTER TABLE `TURNO`
  MODIFY `turno_id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `TURNOTRABAJO`
--
ALTER TABLE `TURNOTRABAJO`
  MODIFY `TURNOTRABAJO_Id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `USUARIO`
--
ALTER TABLE `USUARIO`
  MODIFY `usuario_id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `VENTANILLA`
--
ALTER TABLE `VENTANILLA`
  MODIFY `ventanilla_id` int NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `ATENCION`
--
ALTER TABLE `ATENCION`
  ADD CONSTRAINT `fk_atencion_funcionario` FOREIGN KEY (`funcionario_id`) REFERENCES `FUNCIONARIO` (`funcionario_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_atencion_turno` FOREIGN KEY (`turno_id`) REFERENCES `TURNO` (`turno_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_atencion_ventanilla` FOREIGN KEY (`ventanilla_id`) REFERENCES `VENTANILLA` (`ventanilla_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Filtros para la tabla `FUNCIONARIO`
--
ALTER TABLE `FUNCIONARIO`
  ADD CONSTRAINT `fk_funcionario_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `USUARIO` (`usuario_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Filtros para la tabla `TURNO`
--
ALTER TABLE `TURNO`
  ADD CONSTRAINT `fk_turno_estado` FOREIGN KEY (`estado_id`) REFERENCES `ESTADO_TURNO` (`estado_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_turno_servicio` FOREIGN KEY (`servicio_id`) REFERENCES `SERVICIO` (`servicio_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_turno_ventanilla` FOREIGN KEY (`ventanilla_id`) REFERENCES `VENTANILLA` (`ventanilla_id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Filtros para la tabla `TURNO_TTRABAJO`
--
ALTER TABLE `TURNO_TTRABAJO`
  ADD CONSTRAINT `TURNO_TTRABAJO_ibfk_1` FOREIGN KEY (`TURNO_Id`) REFERENCES `TURNO` (`turno_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  ADD CONSTRAINT `TURNO_TTRABAJO_ibfk_2` FOREIGN KEY (`TURNO_TRABAJO_Id`) REFERENCES `TURNOTRABAJO` (`TURNOTRABAJO_Id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

--
-- Filtros para la tabla `USUARIO`
--
ALTER TABLE `USUARIO`
  ADD CONSTRAINT `fk_usuario_rol` FOREIGN KEY (`rol_id`) REFERENCES `ROL` (`rol_id`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Filtros para la tabla `VENTANILLA_SERVICIO`
--
ALTER TABLE `VENTANILLA_SERVICIO`
  ADD CONSTRAINT `fk_vs_servicio` FOREIGN KEY (`servicio_id`) REFERENCES `SERVICIO` (`servicio_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_vs_ventanilla` FOREIGN KEY (`ventanilla_id`) REFERENCES `VENTANILLA` (`ventanilla_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
