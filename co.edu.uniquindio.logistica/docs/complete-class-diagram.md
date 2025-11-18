# Diagrama de Clases Completo - Sistema de Logística

## Relaciones de Herencia e Implementación

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
<<abstract>>
UsuarioBuilder
├── +nombre(String): UsuarioBuilder
├── +correo(String): UsuarioBuilder
├── +telefono(String): UsuarioBuilder
├── +direccion(Direccion): UsuarioBuilder
└── +build(): Usuario

DirectorUsuario
├── -builder: UsuarioBuilder
├── +DirectorUsuario(builder: UsuarioBuilder)
└── +construirUsuarioEstandar(nombre: String, correo: String, telefono: String): Usuario
```

### 3. Adapter Pattern
```
<<interface>>
PaymentGateway
└── +procesarPago(cardNumber: String, amount: double): boolean

ExternalPaymentService
├── +makePayment(card: String, amount: double): boolean

PaymentGatewayAdapter implements PaymentGateway
├── -externalService: ExternalPaymentService
├── +PaymentGatewayAdapter(ExternalPaymentService)
└── +procesarPago(cardNumber: String, amount: double): boolean
```

### 4. Decorator Pattern
```
<<interface>>
EnvioService
├── +calcularCosto(): double
├── +getDescripcion(): String
└── +getTiempoEntrega(): int

EnvioBasico implements EnvioService
├── -envio: Envio
├── +EnvioBasico(envio: Envio)
├── +calcularCosto(): double
├── +getDescripcion(): String
└── +getTiempoEntrega(): int

<<abstract>>
EnvioDecorator implements EnvioService
├── -envioService: EnvioService
├── +EnvioDecorator(envioService: EnvioService)
├── +calcularCosto(): double
├── +getDescripcion(): String
└── +getTiempoEntrega(): int

SeguroDecorator extends EnvioDecorator
├── +SeguroDecorator(envioService: EnvioService)
├── +calcularCosto(): double
├── +getDescripcion(): String
└── +getTiempoEntrega(): int

EntregaRapidaDecorator extends EnvioDecorator
├── +EntregaRapidaDecorator(envioService: EnvioService)
├── +calcularCosto(): double
├── +getDescripcion(): String
└── +getTiempoEntrega(): int

EmbalajeEspecialDecorator extends EnvioDecorator
├── +EmbalajeEspecialDecorator(envioService: EnvioService)
├── +calcularCosto(): double
├── +getDescripcion(): String
└── +getTiempoEntrega(): int
```

### 5. Observer Pattern
```
<<interface>>
ShipmentObserver
└── +update(shipmentId: String, oldStatus: ShipmentStatus, newStatus: ShipmentStatus): void

<<interface>>
ShipmentSubject
├── +addObserver(observer: ShipmentObserver): void
├── +removeObserver(observer: ShipmentObserver): void
└── +notifyObservers(oldStatus: ShipmentStatus, newStatus: ShipmentStatus): void

abstract class ShipmentSubjectBase implements ShipmentSubject
├── -observers: List<ShipmentObserver>
├── -shipmentId: String
├── +ShipmentSubjectBase(shipmentId: String)
├── +addObserver(observer: ShipmentObserver): void
├── +removeObserver(observer: ShipmentObserver): void
├── +notifyObservers(oldStatus: ShipmentStatus, newStatus: ShipmentStatus): void
└── abstract updateStatus(newStatus: ShipmentStatus): void

ObservableShipment extends ShipmentSubjectBase
├── -envio: Envio
├── +ObservableShipment(envio: Envio)
└── +updateStatus(newStatus: ShipmentStatus): void

ConsoleLoggerObserver implements ShipmentObserver
└── +update(shipmentId: String, oldStatus: ShipmentStatus, newStatus: ShipmentStatus): void

HistoryTrackerObserver implements ShipmentObserver
└── +update(shipmentId: String, oldStatus: ShipmentStatus, newStatus: ShipmentStatus): void
```

### 6. Command Pattern
```
<<interface>>
AdminCommand
├── +execute(): CommandResult
├── +undo(): CommandResult
└── +getDescription(): String

CreateUserCommand implements AdminCommand
├── -adminService: IAdminService
├── -userData: Usuario
├── +CreateUserCommand(adminService: IAdminService, userData: Usuario)
├── +execute(): CommandResult
├── +undo(): CommandResult
└── +getDescription(): String

UpdateShipmentStatusCommand implements AdminCommand
├── -store: InMemoryStore
├── -shipmentId: String
├── -newStatus: ShipmentStatus
├── -oldStatus: ShipmentStatus
├── +UpdateShipmentStatusCommand(store: InMemoryStore, shipmentId: String, newStatus: ShipmentStatus)
├── +execute(): CommandResult
├── +undo(): CommandResult
└── +getDescription(): String

CommandInvoker
├── -commandHistory: Stack<AdminCommand>
├── +executeCommand(command: AdminCommand): CommandResult
├── +undoLastCommand(): CommandResult
└── +getCommandHistory(): List<String>
```

### 7. Facade Pattern
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
├── -nombre: String
├── -correo: String
├── -telefono: String
├── +Usuario(idUsuario: String, nombre: String, correo: String, telefono: String)
├── +getIdUsuario(): String
├── +getNombre(): String
├── +getCorreo(): String
├── +getTelefono(): String
└── +toString(): String

Envio
├── -idEnvio: String
├── -idUsuario: String
├── -idRepartidor: String
├── -idOrigen: String
├── -idDestino: String
├── -pesoKg: double
├── -volumenM3: double
├── -costo: double
├── -estado: ShipmentStatus
├── -fechaCreacion: LocalDateTime
├── +Envio(idEnvio: String, idUsuario: String, idOrigen: String, idDestino: String, pesoKg: double, volumenM3: double)
└── [getters y setters]

Repartidor
├── -idRepartidor: String
├── -nombre: String
├── -documento: String
├── -telefono: String
├── -disponibilidad: DriverStatus
├── -zona: String
├── +Repartidor(idRepartidor: String, nombre: String, documento: String, telefono: String, zona: String)
└── [getters y setters]

Direccion
├── -idDireccion: String
├── -alias: String
├── -calle: String
├── -ciudad: String
├── -idUsuario: String
├── +Direccion(idDireccion: String, alias: String, calle: String, ciudad: String, idUsuario: String)
└── [getters y setters]

Pago
├── -idPago: String
├── -idEnvio: String
├── -monto: double
├── -fechaPago: LocalDateTime
├── -estado: PaymentStatus
├── -numeroTarjeta: String
├── +Pago(idPago: String, idEnvio: String, monto: double, numeroTarjeta: String)
└── [getters y setters]

Tarifa
├── -idTarifa: String
├── -origen: String
├── -destino: String
├── -costoBase: double
├── -costoPorKg: double
├── -costoPorM3: double
├── +Tarifa(idTarifa: String, origen: String, destino: String, costoBase: double, costoPorKg: double, costoPorM3: double)
└── [getters y setters]
```

## Interfaces de Servicio

```
<<interface>>
IUserService
├── +crearUsuario(usuario: Usuario): Usuario
├── +obtenerUsuario(id: String): Optional<Usuario>
├── +actualizarUsuario(usuario: Usuario): Usuario
├── +eliminarUsuario(id: String): boolean
├── +listarUsuarios(): List<Usuario>
└── +agregarDireccionUsuario(usuarioId: String, direccion: Direccion): Direccion

<<interface>>
IAdminService
├── +gestionarUsuarios(usuario: Usuario, accion: String): boolean
├── +gestionarConductores(conductor: Repartidor, accion: String): boolean
├── +gestionarEnvios(envio: Envio, accion: String): boolean
├── +generarReportes(): String
└── +obtenerMetricasSistema(): String

<<interface>>
IPaymentService
├── +procesarPago(envioId: String, cardNumber: String, amount: double): boolean
└── +obtenerEstadoPago(pagoId: String): PaymentStatus

<<interface>>
ITarifaStrategy
├── +calcularTarifa(origen: String, destino: String, pesoKg: double, volumenM3: double): double
```

## Implementaciones de Servicio

```
UserServiceImpl implements IUserService
├── -store: InMemoryStore
├── +UserServiceImpl(store: InMemoryStore)
└── [implementación de métodos IUserService]

AdminServiceImpl implements IAdminService
├── -store: InMemoryStore
├── -commandInvoker: CommandInvoker
├── +AdminServiceImpl(store: InMemoryStore, commandInvoker: CommandInvoker)
└── [implementación de métodos IAdminService]

PaymentServiceImpl implements IPaymentService
├── -paymentGateway: PaymentGateway
├── -store: InMemoryStore
├── +PaymentServiceImpl(paymentGateway: PaymentGateway, store: InMemoryStore)
└── [implementación de métodos IPaymentService]

SimpleTarifaStrategy implements ITarifaStrategy
├── +calcularTarifa(origen: String, destino: String, pesoKg: double, volumenM3: double): double
```

## Repositorio y Almacenamiento

```
<<singleton>>
InMemoryStore
├── -usuarios: Map<String, Usuario>
├── -repartidores: Map<String, Repartidor>
├── -envios: Map<String, Envio>
├── -direcciones: Map<String, Direccion>
├── -pagos: Map<String, Pago>
├── -tarifas: Map<String, Tarifa>
├── -credentials: Map<String, String>
├── -instance: InMemoryStore
├── -nextIdCounter: Map<String, AtomicInteger>
├── +getInstance(): InMemoryStore
├── +nextId(prefix: String): String
└── [métodos CRUD para todas las entidades]
```

## DTOs (Data Transfer Objects)

```
UsuarioDTO
├── -idUsuario: String
├── -nombre: String
├── -correo: String
├── -telefono: String
└── [getters y setters]

EnvioDTO
├── -idEnvio: String
├── -idUsuario: String
├── -idRepartidor: String
├── -idOrigen: String
├── -idDestino: String
├── -pesoKg: double
├── -volumenM3: double
├── -costo: double
├── -estado: String
├── -fechaCreacion: String
└── [getters y setters]

DireccionDTO
├── -idDireccion: String
├── -alias: String
├── -calle: String
├── -ciudad: String
├── -idUsuario: String
└── [getters y setters]

RepartidorDTO
├── -idRepartidor: String
├── -nombre: String
├── -documento: String
├── -telefono: String
├── -disponibilidad: String
├── -zona: String
└── [getters y setters]

PagoDTO
├── -idPago: String
├── -idEnvio: String
├── -monto: double
├── -fechaPago: String
├── -estado: String
├── -numeroTarjeta: String
└── [getters y setters]

TarifaDTO
├── -idTarifa: String
├── -origen: String
├── -destino: String
├── -costoBase: double
├── -costoPorKg: double
├── -costoPorM3: double
└── [getters y setters]
```

## Controladores de Interfaz (JavaFX)

```
LogisticsApplication extends Application
├── +start(stage: Stage): void
└── +main(String[] args): void

LogisticsMainController
├── -content: StackPane
├── +openUser(): void
├── +openProfile(): void
├── +openQuote(): void
├── +openShipments(): void
├── +openAdmin(): void
├── +logout(): void
└── +loadIntoCenter(fxmlPath: String): Node

UserLoginController
├── -correo: TextField
├── -password: PasswordField
├── -status: Label
├── +onLogin(): void
└── +onRegister(): void

AdminLoginController
├── -correo: TextField
├── -password: PasswordField
├── -status: Label
├── -root: VBox
└── +onLogin(): void

[Otros controladores para las diferentes vistas]
```

## Utilidades y Configuración

```
Session
├── -currentUser: String
├── -isAdmin: boolean
├── +loginAsUser(userId: String): void
├── +loginAsAdmin(): void
├── +logout(): void
├── +getCurrentUser(): String
├── +isAdmin(): boolean
└── +isLoggedIn(): boolean

CommandResult
├── -success: boolean
├── -message: String
├── +CommandResult(success: boolean, message: String)
├── +isSuccess(): boolean
├── +getMessage(): String
└── +toString(): String
```

## Enums

```
enum ShipmentStatus
├── PENDIENTE
├── ASIGNADO
├── EN_TRANSITO
├── ENTREGADO
└── CANCELADO

enum DriverStatus
├── DISPONIBLE
├── OCUPADO
└── NO_DISPONIBLE

enum PaymentStatus
├── PENDIENTE
├── APROBADO
├── RECHAZADO
└── REEMBOLSADO

enum ServiceExtraType
├── SEGURO
├── ENTREGA_RAPIDA
└── EMBALAJE_ESPECIAL
```

## Relaciones Completas del Sistema

### Relaciones de Composición
- **LogisticsFacade** → InMemoryStore, PaymentGateway, IUserService, IAdminService
- **CommandInvoker** → Stack<AdminCommand>
- **ShipmentSubjectBase** → List<ShipmentObserver>
- **EnvioDecorator** → EnvioService
- **InMemoryStore** → Maps de todas las entidades

### Relaciones de Herencia
- **EnvioEstandardFactory**, **EnvioPrioritarioFactory** → **EnvioFactory**
- **SeguroDecorator**, **EntregaRapidaDecorator**, **EmbalajeEspecialDecorator** → **EnvioDecorator**
- **ObservableShipment** → **ShipmentSubjectBase**
- **LogisticsApplication** → **Application** (JavaFX)

### Relaciones de Implementación
- **PaymentGatewayAdapter** → **PaymentGateway**
- **EnvioBasico**, **EnvioDecorator** → **EnvioService**
- **CreateUserCommand**, **UpdateShipmentStatusCommand** → **AdminCommand**
- **ConsoleLoggerObserver**, **HistoryTrackerObserver** → **ShipmentObserver**
- **UserServiceImpl** → **IUserService**
- **AdminServiceImpl** → **IAdminService**
- **PaymentServiceImpl** → **IPaymentService**
- **SimpleTarifaStrategy** → **ITarifaStrategy**
- **ShipmentSubjectBase** → **ShipmentSubject**

Este diagrama completo muestra todas las clases del proyecto con sus relaciones bien estructuradas, siguiendo los patrones de diseño implementados y las arquitecturas establecidas.
