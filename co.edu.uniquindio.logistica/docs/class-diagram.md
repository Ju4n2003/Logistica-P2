# Diagrama de Clases - Sistema de Logística

## Estructura General del Sistema

El sistema de logística implementa múltiples patrones de diseño para gestionar envíos, usuarios y operaciones administrativas. A continuación se presenta el diagrama de clases organizado por patrones y componentes principales.

## Patrones de Diseño Implementados

### 1. Factory Method Pattern
```
<<abstract>>
EnvioFactory
├── +crearEnvio(data: EnvioData): Envio
├── +calcularCostoBase(data: EnvioData): double
└── record EnvioData(origenId, destinoId, pesoKg, volumenM3, usuarioId, serviciosAdicionales)

EnvioEstandardFactory extends EnvioFactory
├── +crearEnvio(data: EnvioData): Envio
└── +calcularCostoBase(data: EnvioData): double

EnvioPrioritarioFactory extends EnvioFactory
├── +crearEnvio(data: EnvioData): Envio
└── +calcularCostoBase(data: EnvioData): double
```

### 2. Builder Pattern
```
UsuarioBuilder
├── -idUsuario: String
├── -nombreCompleto: String
├── -correo: String
├── -telefono: String
├── -direcciones: List<Direccion>
├── +conId(id: String): UsuarioBuilder
├── +conNombre(nombre: String): UsuarioBuilder
├── +conCorreo(correo: String): UsuarioBuilder
├── +conTelefono(telefono: String): UsuarioBuilder
├── +conDireccion(direccion: Direccion): UsuarioBuilder
├── +build(): Usuario
└── +reset(): void

DirectorUsuario
├── -builder: UsuarioBuilder
├── +construirUsuarioBasico(): Usuario
├── +construirUsuarioPremium(): Usuario
└── +construirUsuarioCompleto(): Usuario
```

### 3. Singleton Pattern
```
<<singleton>>
InMemoryStore
├── -instance: InMemoryStore
├── -usuarios: Map<String, Usuario>
├── -envios: Map<String, Envio>
├── -direcciones: Map<String, Direccion>
├── -conductores: Map<String, Conductor>
├── +getInstance(): InMemoryStore
├── +usuarios(): Map<String, Usuario>
├── +envios(): Map<String, Envio>
├── +direcciones(): Map<String, Direccion>
├── +conductores(): Map<String, Conductor>
└── -generateId(prefix: String): String
```

### 4. Adapter Pattern
```
<<interface>>
PaymentGateway
├── +processPayment(amount: double, cardNumber: String): boolean

ExternalPaymentService
├── -MERCHANT_ID: String
├── +chargeCard(merchantId: String, amount: double, cardInfo: String): boolean

PaymentGatewayAdapter implements PaymentGateway
├── -externalService: ExternalPaymentService
├── +processPayment(amount: double, cardNumber: String): boolean
└── -convertCardFormat(cardNumber: String): String
```

### 5. Decorator Pattern
```
<<interface>>
EnvioService
├── +calcularCosto(): double
├── +getDescripcion(): String
├── +estimarTiempoEntrega(): int

EnvioBasico implements EnvioService
├── -envio: Envio
├── +calcularCosto(): double
├── +getDescripcion(): String
└── +estimarTiempoEntrega(): int

<<abstract>>
EnvioDecorator implements EnvioService
├── -envioService: EnvioService
├── +calcularCosto(): double
├── +getDescripcion(): String
└── +estimarTiempoEntrega(): int

SeguroDecorator extends EnvioDecorator
├── +calcularCosto(): double
└── +getDescripcion(): String

EntregaRapidaDecorator extends EnvioDecorator
├── +calcularCosto(): double
├── +getDescripcion(): String
└── +estimarTiempoEntrega(): int

EmbalajeEspecialDecorator extends EnvioDecorator
├── +calcularCosto(): double
└── +getDescripcion(): String
```

### 6. Observer Pattern
```
<<interface>>
ShipmentSubject
├── +addObserver(observer: ShipmentObserver): void
├── +removeObserver(observer: ShipmentObserver): void
├── +notifyObservers(oldStatus: String, newStatus: String): void

ShipmentSubjectBase implements ShipmentSubject
├── -observers: List<ShipmentObserver>
├── +addObserver(observer: ShipmentObserver): void
├── +removeObserver(observer: ShipmentObserver): void
└── +notifyObservers(oldStatus: String, newStatus: String): void

<<interface>>
ShipmentObserver
└── +update(shipmentId: String, oldStatus: String, newStatus: String): void

ObservableShipment extends ShipmentSubjectBase
├── -envio: Envio
├── +ObservableShipment(envio: Envio)
├── +updateStatus(newStatus: ShipmentStatus): void
├── +getEnvio(): Envio
└── +getCurrentStatus(): ShipmentStatus

ConsoleLoggerObserver implements ShipmentObserver
└── +update(shipmentId: String, oldStatus: String, newStatus: String): void

HistoryTrackerObserver implements ShipmentObserver
├── -eventHistory: List<ShipmentStatusChangeEvent>
├── +update(shipmentId: String, oldStatus: String, newStatus: String): void
├── +getEventHistory(): List<ShipmentStatusChangeEvent>
├── +getEventsForShipment(shipmentId: String): List<ShipmentStatusChangeEvent>
└── +clearHistory(): void
```

### 7. Command Pattern
```
<<interface>>
AdminCommand
├── +execute(): CommandResult
├── +undo(): CommandResult
└── +getDescription(): String

CommandResult
├── -success: boolean
├── -message: String
├── +isSuccess(): boolean
└── +getMessage(): String

CreateUserCommand implements AdminCommand
├── -store: InMemoryStore
├── -nombre: String
├── -email: String
├── -telefono: String
├── -createdUserId: String
├── +execute(): CommandResult
├── +undo(): CommandResult
└── +getDescription(): String

UpdateShipmentStatusCommand implements AdminCommand
├── -store: InMemoryStore
├── -shipmentId: String
├── -newStatus: ShipmentStatus
├── -oldStatus: ShipmentStatus
├── +execute(): CommandResult
├── +undo(): CommandResult
└── +getDescription(): String

CommandInvoker
├── -commandHistory: List<AdminCommand>
├── -undoHistory: List<AdminCommand>
├── +executeCommand(command: AdminCommand): CommandResult
├── +undoLastCommand(): CommandResult
├── +redoLastCommand(): CommandResult
├── +getCommandHistory(): List<String>
├── +clearHistory(): void
└── +getStats(): CommandStats
```

### 8. Facade Pattern
```
LogisticsFacade
├── -store: InMemoryStore
├── -paymentGateway: PaymentGateway
├── -userService: IUserService
├── -adminService: IAdminService
├── +LogisticsFacade()
├── +crearEnvioCompleto(data: EnvioFactory.EnvioData): String
├── +procesarPagoEnvio(shipmentId: String, cardNumber: String): boolean
├── +aplicarServiciosAdicionales(shipmentId: String, servicios: Set<ServiceExtraType>): double
├── +crearUsuarioConDireccion(nombre: String, correo: String, telefono: String, direccion: Direccion): String
├── +obtenerMetricasCompletas(): String
├── +obtenerHistorialUsuario(usuarioId: String): String
├── +registrarObservadorEnvio(shipmentId: String, observer: ShipmentObserver): void
└── -actualizarEstadoEnvio(shipmentId: String, nuevoEstado: ShipmentStatus): void
```

## Entidades del Dominio

```
Usuario
├── -idUsuario: String
├── -nombreCompleto: String
├── -correo: String
├── -telefono: String
├── -direcciones: List<Direccion>
├── +getIdUsuario(): String
├── +setIdUsuario(idUsuario: String): void
├── +getNombreCompleto(): String
├── +setNombreCompleto(nombreCompleto: String): void
├── +getCorreo(): String
├── +setCorreo(correo: String): void
├── +getTelefono(): String
├── +setTelefono(telefono: String): void
├── +getDirecciones(): List<Direccion>
└── +setDirecciones(direcciones: List<Direccion>): void

Envio
├── -idEnvio: String
├── -origen: Direccion
├── -destino: Direccion
├── -pesoKg: double
├── -volumenM3: double
├── -estado: ShipmentStatus
├── -usuarioId: String
├── -serviciosAdicionales: Set<ServiceExtraType>
├── +getIdEnvio(): String
├── +setIdEnvio(idEnvio: String): void
├── +getOrigen(): Direccion
├── +setOrigen(origen: Direccion): void
├── +getDestino(): Direccion
├── +setDestino(destino: Direccion): void
├── +getPesoKg(): double
├── +setPesoKg(pesoKg: double): void
├── +getVolumenM3(): double
├── +setVolumenM3(volumenM3: double): void
├── +getEstado(): ShipmentStatus
├── +setEstado(estado: ShipmentStatus): void
├── +getUsuarioId(): String
├── +setUsuarioId(usuarioId: String): void
├── +getServiciosAdicionales(): Set<ServiceExtraType>
└── +setServiciosAdicionales(serviciosAdicionales: Set<ServiceExtraType>): void

Direccion
├── -idDireccion: String
├── -calle: String
├── -ciudad: String
├── -departamento: String
├── -codigoPostal: String
├── -pais: String
├── +getIdDireccion(): String
├── +setIdDireccion(idDireccion: String): void
├── +getCalle(): String
├── +setCalle(calle: String): void
├── +getCiudad(): String
├── +setCiudad(ciudad: String): void
├── +getDepartamento(): String
├── +setDepartamento(departamento: String): void
├── +getCodigoPostal(): String
├── +setCodigoPostal(codigoPostal: String): void
├── +getPais(): String
└── +setPais(pais: String): void

Conductor
├── -idConductor: String
├── -nombre: String
├── -licencia: String
├── -telefono: String
├── -vehiculo: String
├── +getIdConductor(): String
├── +setIdConductor(idConductor: String): void
├── +getNombre(): String
├── +setNombre(nombre: String): void
├── +getLicencia(): String
├── +setLicencia(licencia: String): void
├── +getTelefono(): String
├── +setTelefono(telefono: String): void
├── +getVehiculo(): String
└── +setVehiculo(vehiculo: String): void
```

## Enums

```
ShipmentStatus
├── SOLICITADO
├── ASIGNADO
├── EN_RUTA
├── ENTREGADO
└── INCIDENCIA

ServiceExtraType
├── SEGURO
├── FRAGIL
├── FIRMA_REQUERIDA
└── PRIORIDAD
```

## Interfaces de Servicio

```
IUserService
├── +login(correo: String, password: String): boolean
├── +registrarUsuario(usuario: Usuario): boolean
├── +actualizarPerfil(usuario: Usuario): boolean
├── +obtenerHistorialEnvios(usuarioId: String): List<Envio>
└── +recuperarContraseña(correo: String): boolean

IPaymentService
├── +procesarPago(monto: double, metodoPago: String): boolean
├── +reembolsar(transaccionId: String): boolean
└── +obtenerHistorialPagos(usuarioId: String): List<String>

IAdminService
├── +gestionarUsuarios(usuario: Usuario, accion: String): boolean
├── +gestionarConductores(conductor: Conductor, accion: String): boolean
├── +gestionarEnvios(envio: Envio, accion: String): boolean
├── +generarReportes(): String
└── +obtenerMetricasSistema(): String
```

## Relaciones Clave

- **LogisticsFacade** utiliza Factory Method, Builder, Adapter y Decorator
- **ObservableShipment** implementa Observer para notificaciones de estado
- **CommandInvoker** gestiona comandos administrativos con capacidad de undo/redo
- **InMemoryStore** proporciona almacenamiento centralizado mediante Singleton
- **PaymentGatewayAdapter** adapta servicios externos de pago

Este diseño permite un sistema modular, extensible y mantenible que sigue los principios SOLID y patrones de diseño establecidos.
