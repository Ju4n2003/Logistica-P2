# Plataforma de Logística y Envíos Urbanos 🚚🏙️

## Overview 📖
Aplicación de escritorio (JavaFX) para logística urbana tipo same-day. Permite a usuarios:
- Registrarse/iniciar sesión, gestionar perfil y direcciones frecuentes.
- Cotizar tarifas por origen/destino, peso/volumen y prioridad; crear envíos y pagarlos de forma simulada.
- Rastrear estado de sus envíos, consultar historial con filtros y exportar reportes CSV/PDF.

Y a administradores (operaciones):
- Gestionar usuarios y repartidores (CRUD, disponibilidad, zona).
- Asignar/reasignar envíos, cambiar estados y registrar incidencias.
- Visualizar métricas con gráficos (estados, ingresos/mes, envíos/día) y exportar CSV/PDF.

## Features ✨
- Autenticación básica de usuario (registro/login).
- Perfil y Direcciones frecuentes (CRUD).
- Cotización de tarifas (Strategy) y creación de envíos con servicios adicionales (base para Decorator).
- Tracking de envíos y pagos simulados.
- Historial con filtros (fecha/estado) + exportes CSV/PDF.
- Admin: CRUD de Usuarios y Repartidores.
- Admin: Asignaciones/Reasignaciones, cambios de estado, incidencias.
- Admin: Métricas con JavaFX Charts + exportes CSV/PDF.

   Compila, descarga dependencias y lanza la app con el módulo de logística.

## Estructura 📂
Ruta: `co.edu.uniquindio.logistica/`

- `src/main/java/`
  - `co/edu/uniquindio/logistica/logistics/`
    - `LogisticsApplication` (punto de entrada JavaFX)
    - `Session` (sesión de usuario)
    - `model/` (Usuario, Repartidor, Envio, Direccion, Tarifa, Pago, enums)
    - `dto/` (EnvioDTO, TarifaDTO, PagoDTO, UsuarioDTO, RepartidorDTO, DireccionDTO)
    - `service/` (`IUserService`, `IAdminService`, `ITarifaStrategy`, `IPaymentService` + impl)
    - `repository/` (`InMemoryStore`)
    - `viewController/` (controladores JavaFX)
- `src/main/resources/logistics/` (FXML UI: `logistics_main.fxml`, `user_*`, `admin_*`)
- `exports/` (CSV/PDF generados)
- `pom.xml` (Maven, Java 21, JavaFX, PDFBox)

## Autenticación 🔐
- Usuario: Login/Registro en `Usuario` → Login. Credenciales creadas desde Admin o durante registro.
- Administrador: Botón `Admin` → Login de admin.
  - Demo: `admin@logistica.com` / `admin`.
- Gestión de credenciales: Admin → Usuarios
  - Al crear usuario puedes ingresar contraseña para guardar credencial.
  - También puedes establecer/actualizar la clave con el botón "Establecer clave".