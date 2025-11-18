# Documentación de Patrones de Diseño - Sistema de Logística

## Introducción

Este documento describe la implementación de patrones de diseño en el sistema de logística. Cada patrón fue seleccionado para resolver problemas específicos de diseño y arquitectura, siguiendo los principios SOLID y las mejores prácticas de desarrollo de software.

## 1. Factory Method Pattern

### Propósito
Definir una interfaz para crear objetos, pero permitir que las subclases decidan qué clase instanciar. Permite que una clase delegue la instanciación a sus subclases.

### Problema Resuelto
- Necesidad de crear diferentes tipos de envíos (estándar y prioritario) con lógica de cálculo de costos diferente
- Evitar acoplamiento entre el código cliente y las clases concretas de envíos
- Facilitar la adición de nuevos tipos de envíos en el futuro

### Implementación
```java
// Clase abstracta Factory
public abstract class EnvioFactory {
    public abstract Envio crearEnvio(EnvioData data);
    public abstract double calcularCostoBase(EnvioData data);
    
    public record EnvioData(
        String origenId, String destinoId, double pesoKg, 
        double volumenM3, String usuarioId, 
        Set<ServiceExtraType> serviciosAdicionales
    ) {}
}

// Factory concretos
public class EnvioEstandardFactory extends EnvioFactory {
    @Override
    public Envio crearEnvio(EnvioData data) {
        // Lógica específica para envíos estándar
    }
}

public class EnvioPrioritarioFactory extends EnvioFactory {
    @Override
    public Envio crearEnvio(EnvioData data) {
        // Lógica específica para envíos prioritarios
    }
}
```

### Ventajas
- **Desacoplamiento**: El cliente no conoce las clases concretas de envíos
- **Extensibilidad**: Fácil agregar nuevos tipos de envíos
- **Mantenimiento**: Centraliza la lógica de creación
- **Testabilidad**: Facilita las pruebas unitarias

### Uso en el Sistema
- `LogisticsFacade` utiliza factories para crear envíos según el tipo requerido
- Cada factory encapsula la lógica de cálculo de costos específica

## 2. Builder Pattern

### Propósito
Separar la construcción de un objeto complejo de su representación, permitiendo el mismo proceso de construcción para crear diferentes representaciones.

### Problema Resuelto
- Construcción de objetos `Usuario` con múltiples atributos opcionales
- Evitar constructores con demasiados parámetros (telescoping constructor anti-pattern)
- Proporcionar una API fluida y legible para la construcción de objetos

### Implementación
```java
public class UsuarioBuilder {
    private String idUsuario;
    private String nombreCompleto;
    private String correo;
    private String telefono;
    private List<Direccion> direcciones = new ArrayList<>();
    
    public UsuarioBuilder conId(String id) {
        this.idUsuario = id;
        return this;
    }
    
    public UsuarioBuilder conNombre(String nombre) {
        this.nombreCompleto = nombre;
        return this;
    }
    
    // Otros métodos...
    
    public Usuario build() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        usuario.setNombreCompleto(nombreCompleto);
        // Configurar otros atributos...
        return usuario;
    }
}

// Director para construcciones predefinidas
public class DirectorUsuario {
    public Usuario construirUsuarioBasico() {
        return new UsuarioBuilder()
            .conId("basic-" + UUID.randomUUID())
            .conNombre("Usuario Básico")
            .build();
    }
}
```

### Ventajas
- **Legibilidad**: Código más claro y expresivo
- **Flexibilidad**: Construcción paso a paso con validación
- **Inmutabilidad**: Puede crear objetos inmutables
- **Reutilización**: Director permite construcciones predefinidas

### Uso en el Sistema
- `LogisticsFacade.crearUsuarioConDireccion()` utiliza el builder para usuarios complejos
- `DirectorUsuario` proporciona plantillas de construcción predefinidas

## 3. Singleton Pattern

### Propósito
Asegurar que una clase tenga una única instancia y proporcionar un punto de acceso global a ella.

### Problema Resuelto
- Necesidad de un almacenamiento centralizado para datos del sistema
- Evitar múltiples instancias del repositorio que podrían causar inconsistencias
- Proporcionar acceso global a los datos del sistema

### Implementación
```java
public class InMemoryStore {
    private static volatile InMemoryStore instance;
    
    private final Map<String, Usuario> usuarios = new ConcurrentHashMap<>();
    private final Map<String, Envio> envios = new ConcurrentHashMap<>();
    // otros mapas...
    
    private InMemoryStore() {
        // Constructor privado para evitar instanciación externa
    }
    
    public static InMemoryStore getInstance() {
        if (instance == null) {
            synchronized (InMemoryStore.class) {
                if (instance == null) {
                    instance = new InMemoryStore();
                }
            }
        }
        return instance;
    }
    
    // Métodos de acceso a los datos...
}
```

### Ventajas
- **Control de Acceso**: Punto único de acceso a los datos
- **Memoria**: Solo una instancia, ahorro de recursos
- **Coherencia**: Evita inconsistencias por múltiples instancias
- **Thread Safety**: Implementación thread-safe con double-checked locking

### Uso en el Sistema
- Todas las clases acceden a los datos a través de `InMemoryStore.getInstance()`
- Centraliza el almacenamiento de usuarios, envíos, direcciones y conductores

## 4. Adapter Pattern

### Propósito
Convertir la interfaz de una clase en otra interfaz que el cliente espera. Permite que clases con interfaces incompatibles trabajen juntas.

### Problema Resuelto
- Integración con servicios de pago externos con interfaces diferentes
- Necesidad de mantener el sistema desacoplado de implementaciones externas
- Facilitar el cambio de proveedores de pago sin modificar el código principal

### Implementación
```java
// Interfaz target que el cliente espera
public interface PaymentGateway {
    boolean processPayment(double amount, String cardNumber);
}

// Clase externa que necesita ser adaptada
public class ExternalPaymentService {
    private static final String MERCHANT_ID = "MERCHANT_123";
    
    public boolean chargeCard(String merchantId, double amount, String cardInfo) {
        // Lógica específica del servicio externo
    }
}

// Adapter que convierte la interfaz
public class PaymentGatewayAdapter implements PaymentGateway {
    private final ExternalPaymentService externalService;
    
    public PaymentGatewayAdapter() {
        this.externalService = new ExternalPaymentService();
    }
    
    @Override
    public boolean processPayment(double amount, String cardNumber) {
        String cardInfo = convertCardFormat(cardNumber);
        return externalService.chargeCard("MERCHANT_123", amount, cardInfo);
    }
    
    private String convertCardFormat(String cardNumber) {
        // Conversión del formato de tarjeta
    }
}
```

### Ventajas
- **Desacoplamiento**: El sistema no depende de implementaciones externas
- **Flexibilidad**: Fácil cambiar de proveedor de servicios
- **Reutilización**: Reutilizar clases existentes con interfaces incompatibles
- **Transparencia**: El cliente no sabe que está usando un adapter

### Uso en el Sistema
- `LogisticsFacade` utiliza `PaymentGatewayAdapter` para procesar pagos
- Permite cambiar de proveedor de pago sin modificar el facade

## 5. Decorator Pattern

### Propósito
Añadir responsabilidades adicionales a objetos dinámicamente. Los decoradores proporcionan una alternativa flexible a la herencia para extender funcionalidad.

### Problema Resuelto
- Necesidad de agregar servicios adicionales a los envíos de forma dinámica
- Evitar explosión de clases por combinaciones de servicios
- Calcular costos adicionales basados en servicios seleccionados

### Implementación
```java
// Componente base
public interface EnvioService {
    double calcularCosto();
    String getDescripcion();
    int estimarTiempoEntrega();
}

// Componente concreto
public class EnvioBasico implements EnvioService {
    private final Envio envio;
    
    @Override
    public double calcularCosto() {
        return envio.getPesoKg() * 1000 + envio.getVolumenM3() * 2000;
    }
    
    @Override
    public String getDescripcion() {
        return "Envío básico";
    }
}

// Decorador base
public abstract class EnvioDecorator implements EnvioService {
    protected final EnvioService envioService;
    
    public EnvioDecorator(EnvioService envioService) {
        this.envioService = envioService;
    }
}

// Decoradores concretos
public class SeguroDecorator extends EnvioDecorator {
    @Override
    public double calcularCosto() {
        return envioService.calcularCosto() + 50000;
    }
    
    @Override
    public String getDescripcion() {
        return envioService.getDescripcion() + " + Seguro";
    }
}

public class EntregaRapidaDecorator extends EnvioDecorator {
    @Override
    public double calcularCosto() {
        return envioService.calcularCosto() * 1.5;
    }
    
    @Override
    public int estimarTiempoEntrega() {
        return envioService.estimarTiempoEntrega() / 2;
    }
}
```

### Ventajas
- **Flexibilidad**: Agregar funcionalidad dinámicamente
- **Composición**: Evitar explosión de clases por combinaciones
- **Open/Closed**: Abierto a extensión, cerrado a modificación
- **Reutilización**: Los decoradores pueden reutilizarse

### Uso en el Sistema
- `LogisticsFacade.aplicarServiciosAdicionales()` utiliza decoradores
- Cada servicio adicional (seguro, entrega rápida, embalaje) es un decorador

## 6. Observer Pattern

### Propósito
Definir una dependencia uno-a-muchos entre objetos para que cuando un objeto cambia de estado, todos sus dependientes sean notificados y actualizados automáticamente.

### Problema Resuelto
- Necesidad de notificar cambios de estado de los envíos
- Desacoplar el código que genera eventos del código que los procesa
- Permitir múltiples observadores para un mismo envío

### Implementación
```java
// Subject (Sujeto)
public interface ShipmentSubject {
    void addObserver(ShipmentObserver observer);
    void removeObserver(ShipmentObserver observer);
    void notifyObservers(String oldStatus, String newStatus);
}

// Concrete Subject
public class ObservableShipment extends ShipmentSubjectBase {
    private Envio envio;
    
    public void updateStatus(ShipmentStatus newStatus) {
        String oldStatus = envio.getEstado().toString();
        envio.setEstado(newStatus);
        notifyObservers(oldStatus, newStatus.toString());
    }
}

// Observer (Observador)
public interface ShipmentObserver {
    void update(String shipmentId, String oldStatus, String newStatus);
}

// Concrete Observers
public class ConsoleLoggerObserver implements ShipmentObserver {
    @Override
    public void update(String shipmentId, String oldStatus, String newStatus) {
        System.out.printf("[%s] Envío %s: %s -> %s%n", 
            LocalDateTime.now(), shipmentId, oldStatus, newStatus);
    }
}

public class HistoryTrackerObserver implements ShipmentObserver {
    private final List<ShipmentStatusChangeEvent> eventHistory = new ArrayList<>();
    
    @Override
    public void update(String shipmentId, String oldStatus, String newStatus) {
        eventHistory.add(new ShipmentStatusChangeEvent(
            shipmentId, oldStatus, newStatus, LocalDateTime.now()
        ));
    }
}
```

### Ventajas
- **Desacoplamiento**: Bajo acoplamiento entre subject y observers
- **Flexibilidad**: Agregar/eliminar observers dinámicamente
- **Broadcast**: Notificación a múltiples observadores simultáneamente
- **Reutilización**: Los observers pueden reutilizarse en diferentes contexts

### Uso en el Sistema
- `ObservableShipment` notifica cambios de estado
- `ConsoleLoggerObserver` registra cambios en consola
- `HistoryTrackerObserver` mantiene historial de eventos

## 7. Command Pattern

### Propósito
Encapsular una solicitud como un objeto, permitiendo parametrizar clientes con diferentes solicitudes, colas o registros, y sostrar operaciones deshacer.

### Problema Resuelto
- Necesidad de ejecutar acciones administrativas con capacidad de deshacer
- Registrar historial de acciones para auditoría
- Desacoplar el objeto que invoca la acción del objeto que la ejecuta

### Implementación
```java
// Command interface
public interface AdminCommand {
    CommandResult execute();
    CommandResult undo();
    String getDescription();
}

// Concrete Commands
public class CreateUserCommand implements AdminCommand {
    private final InMemoryStore store;
    private final String nombre, email, telefono;
    private String createdUserId;
    
    @Override
    public CommandResult execute() {
        // Lógica para crear usuario
        createdUserId = usuario.getIdUsuario();
        return new CommandResult(true, "Usuario creado: " + createdUserId);
    }
    
    @Override
    public CommandResult undo() {
        // Lógica para eliminar usuario creado
        return new CommandResult(true, "Usuario eliminado");
    }
}

// Invoker
public class CommandInvoker {
    private final List<AdminCommand> commandHistory = new ArrayList<>();
    private final List<AdminCommand> undoHistory = new ArrayList<>();
    
    public CommandResult executeCommand(AdminCommand command) {
        CommandResult result = command.execute();
        if (result.isSuccess()) {
            commandHistory.add(command);
            undoHistory.clear();
        }
        return result;
    }
    
    public CommandResult undoLastCommand() {
        if (commandHistory.isEmpty()) {
            return new CommandResult(false, "No hay comandos para deshacer");
        }
        
        AdminCommand lastCommand = commandHistory.remove(commandHistory.size() - 1);
        CommandResult result = lastCommand.undo();
        
        if (result.isSuccess()) {
            undoHistory.add(lastCommand);
        }
        return result;
    }
}
```

### Ventajas
- **Desacoplamiento**: Cliente no conoce detalles de la operación
- **Extensibilidad**: Fácil agregar nuevos comandos
- **Undo/Redo**: Capacidad de deshacer y rehacer operaciones
- **Historial**: Registro de operaciones ejecutadas

### Uso en el Sistema
- `CreateUserCommand` encapsula creación de usuarios
- `UpdateShipmentStatusCommand` encapsula actualización de estados
- `CommandInvoker` gestiona ejecución y deshacer de comandos

## 8. Facade Pattern

### Propósito
Proporcionar una interfaz unificada para un conjunto de interfaces en un subsistema. Facade define una interfaz de nivel superior que hace el subsistema más fácil de usar.

### Problema Resuelto
- Simplificar la interacción con múltiples subsistemas complejos
- Proporcionar una API simple y coherente para operaciones comunes
- Reducir el acoplamiento entre clientes y subsistemas

### Implementación
```java
public class LogisticsFacade {
    private final InMemoryStore store;
    private final PaymentGateway paymentGateway;
    private final IUserService userService;
    private final IAdminService adminService;
    
    public LogisticsFacade() {
        this.store = InMemoryStore.getInstance();
        this.paymentGateway = new PaymentGatewayAdapter();
        this.userService = new UserServiceImpl();
        this.adminService = new AdminServiceImpl();
    }
    
    // Operaciones de alto nivel
    public String crearEnvioCompleto(EnvioFactory.EnvioData data) {
        // Utiliza Factory Method para crear envío
        // Aplica Decorator para servicios adicionales
        // Guarda en InMemoryStore
    }
    
    public boolean procesarPagoEnvio(String shipmentId, String cardNumber) {
        // Utiliza Adapter para procesar pago
        // Actualiza estado del envío
        // Notifica observers
    }
    
    public String crearUsuarioConDireccion(String nombre, String correo, 
                                          String telefono, Direccion direccion) {
        // Utiliza Builder para crear usuario
        // Guarda en InMemoryStore
    }
}
```

### Ventajas
- **Simplicidad**: Interfaz simple para operaciones complejas
- **Desacoplamiento**: Clientes no conocen detalles internos
- **Capa de Abstracción**: Oculta complejidad del subsistema
- **Centralización**: Punto único de acceso a múltiples servicios

### Uso en el Sistema
- `LogisticsFacade` es el punto principal de interacción
- Integra Factory Method, Builder, Adapter, Decorator y Observer
- Simplifica operaciones complejas en métodos simples

## Conclusiones

La implementación de estos patrones de diseño proporciona:

1. **Arquitectura Sólida**: Cada patrón contribuye a los principios SOLID
2. **Mantenibilidad**: Código modular y desacoplado
3. **Extensibilidad**: Fácil agregar nuevas funcionalidades
4. **Testabilidad**: Componentes aislados y fáciles de probar
5. **Reutilización**: Componentes pueden reutilizarse en diferentes contexts

Los patrones trabajan juntos para crear un sistema robusto, flexible y mantenible que puede evolucionar con los requisitos cambiantes del negocio.
