SISTEMA DISTRIBUIDO DE GESTION DE TURNOS - COMO EJECUTAR
=========================================================

1. BASE DE DATOS (MySQL)
   a) Ejecutar "gamabasis_p3g1 (1).sql" (crea las tablas).
   b) Ejecutar "datos_prueba.sql" (servicios, ventanillas, usuarios y el UNIQUE por dia del codigo de turno).
   c) Poner usuario y contraseña de la base en src/main/java/DataAccesObject/ConexionBD.java.
      

2. EJECUTAR (NetBeans: clic derecho sobre la clase -> Run File)
   Paso 1  lanzadores/MainServidor      Servidor Central.
   Paso 2  lanzadores/MainPantalla      Pantalla publica.
   Paso 3  lanzadores/MainOperador      Una vez por cada operador.
   Paso 4  lanzadores/MainGenerador     Terminal que genera turnos.

3. EN VARIAS COMPUTADORAS
   Ejecutar el servidor en una PC y en las demas agregar a los clientes la opcion de VM:
      -Dservidor.host=IP_DEL_SERVIDOR      (y opcional -Dservidor.puerto=12345)
   En NetBeans: Propiedades del proyecto -> Run -> VM Options.


