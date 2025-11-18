package co.edu.uniquindio.logistica.facade;

import co.edu.uniquindio.logistica.model.*;
import co.edu.uniquindio.logistica.service.IAdminService;
import co.edu.uniquindio.logistica.service.IUserService;
import co.edu.uniquindio.logistica.service.impl.AdminServiceImpl;
import co.edu.uniquindio.logistica.service.impl.UserServiceImpl;
import co.edu.uniquindio.logistica.repository.InMemoryStore;
import co.edu.uniquindio.logistica.factory.*;
import co.edu.uniquindio.logistica.builder.*;
import co.edu.uniquindio.logistica.adapter.*;
import co.edu.uniquindio.logistica.decorator.*;
import java.util.List;
import java.util.Set;

/**
 * Facade que simplifica las operaciones complejas del sistema de logística.
 * Proporciona una interfaz unificada para las operaciones más comunes
 * que involucran múltiples servicios y componentes.
 * 
 * Aplicación del patrón Facade:
 * - Problema: Las operaciones complejas requieren interactuar con 
 *   múltiples servicios y componentes (usuarios, envíos, pagos, etc.)
 * - Propósito: Proporcionar una interfaz simplificada que oculte 
 *   la complejidad del subsistema y facilite su uso
 * - Solución: Clase fachada que coordina las llamadas a múltiples 
 *   servicios y expone operaciones de alto nivel
 */
public class LogisticsFacade {
    
    // Inyección de dependencias
    private final InMemoryStore store;
    // Servicios inyectados para futura expansión
    @SuppressWarnings("unused")
    private final IAdminService adminService;
    @SuppressWarnings("unused")
    private final IUserService userService;
    private final EnvioFactory envioEstandardFactory;
    private final EnvioFactory envioPrioritarioFactory;
    private final DirectorUsuario directorUsuario;
    private final PaymentGateway paymentGateway;
    
    /**
     * Constructor que inicializa todos los servicios necesarios
     */
    public LogisticsFacade() {
        this.store = InMemoryStore.getInstance();
        this.adminService = new AdminServiceImpl();
        this.userService = new UserServiceImpl();
        this.envioEstandardFactory = new EnvioEstandardFactory();
        this.envioPrioritarioFactory = new EnvioPrioritarioFactory();
        this.directorUsuario = new DirectorUsuario();
        this.paymentGateway = new PaymentGatewayAdapter(new ExternalPaymentService());
    }
    
    /**
     * Operación completa de creación de envío con todos los servicios necesarios
     * @param datosEnvio Datos básicos del envío
     * @param esPrioritario Si el envío es prioritario
     * @param serviciosAdicionales Servicios adicionales a incluir
     * @return Envío creado y configurado
     */
    public Envio crearEnvioCompleto(String usuarioId, String origenId, String destinoId, 
                                   double pesoKg, double volumenM3, boolean esPrioritario,
                                   Set<ServiceExtraType> serviciosAdicionales) {
        
        // 1. Validar usuario
        Usuario usuario = store.usuarios().get(usuarioId);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado: " + usuarioId);
        }
        
        // 2. Crear envío usando Factory Method
        EnvioFactory factory = esPrioritario ? envioPrioritarioFactory : envioEstandardFactory;
        var factoryData = new EnvioFactory.EnvioData(
            origenId, destinoId, pesoKg, volumenM3, usuarioId, serviciosAdicionales
        );
        Envio envio = factory.crearEnvio(factoryData);
        
        // 3. Aplicar servicios adicionales usando Decorator si es necesario
        if (serviciosAdicionales != null && !serviciosAdicionales.isEmpty()) {
            envio = aplicarServiciosAdicionales(envio, serviciosAdicionales);
        }
        
        // 4. Guardar envío
        store.envios().put(envio.getIdEnvio(), envio);
        
        // 5. Asignar repartidor automáticamente si está disponible
        asignarRepartidorAutomatico(envio);
        
        return envio;
    }
    
    /**
     * Operación completa de registro de usuario con validación y configuración
     */
    public Usuario registrarUsuarioCompleto(String nombre, String correo, String telefono, 
                                           Direccion direccionPrincipal) {
        
        // 1. Validar que el correo no exista
        if (store.credentials().containsKey(correo)) {
            throw new IllegalArgumentException("El correo ya está registrado: " + correo);
        }
        
        // 2. Crear usuario usando Builder
        Usuario usuario = directorUsuario.construirUsuarioConDireccion(
            nombre, correo, telefono, direccionPrincipal
        );
        
        // 3. Guardar usuario y credenciales
        store.usuarios().put(usuario.getIdUsuario(), usuario);
        store.credentials().put(correo, "1234"); // Contraseña temporal
        
        return usuario;
    }
    
    /**
     * Operación completa de procesamiento de pago
     */
    public boolean procesarPagoCompleto(String envioId, String metodoPago, String datosTarjeta) {
        
        // 1. Obtener envío
        Envio envio = store.envios().get(envioId);
        if (envio == null) {
            throw new IllegalArgumentException("Envío no encontrado: " + envioId);
        }
        
        // 2. Procesar pago usando Adapter
        PaymentGateway.PaymentResult resultado = paymentGateway.processPayment(
            "LOGISTICA-001", envio.getCosto(), datosTarjeta
        );
        
        // 3. Crear registro de pago
        Pago pago = new Pago();
        pago.setIdPago(store.nextId("PAG"));
        pago.setMonto(envio.getCosto());
        pago.setMetodo(metodoPago);
        pago.setResultado(convertirResultadoPago(resultado));
        
        // 4. Guardar pago
        store.pagos().put(pago.getIdPago(), pago);
        
        // 5. Actualizar estado del envío si el pago fue exitoso
        if (pago.getResultado() == PaymentResult.APROBADO) {
            envio.setEstado(ShipmentStatus.ASIGNADO);
        }
        
        return pago.getResultado() == PaymentResult.APROBADO;
    }
    
    /**
     * Obtiene métricas completas del sistema
     */
    public String obtenerMetricasCompletas() {
        return "Métricas del sistema disponibles";
    }
    
    /**
     * Obtiene historial completo de un usuario
     */
    public List<Envio> obtenerHistorialUsuario(String usuarioId) {
        return List.of(); // Implementación básica
    }
    
    // Métodos privados de soporte
    
    private Envio aplicarServiciosAdicionales(Envio envio, Set<ServiceExtraType> serviciosAdicionales) {
        // Aquí se podría integrar con el patrón Decorator si se desea
        // Por ahora, se mantiene la lógica existente en el modelo
        envio.setServicios(serviciosAdicionales != null ? 
            java.util.EnumSet.copyOf(serviciosAdicionales) : java.util.EnumSet.noneOf(ServiceExtraType.class));
        
        // Recalcular costo según servicios adicionales
        double costoBase = envio.getCosto();
        double costoAdicional = 0;
        
        for (ServiceExtraType servicio : serviciosAdicionales) {
            switch (servicio) {
                case SEGURO:
                    costoAdicional += envio.getCosto() * 0.02;
                    break;
                case FRAGIL:
                    costoAdicional += 8000;
                    break;
                case FIRMA_REQUERIDA:
                    costoAdicional += 5000;
                    break;
                case PRIORIDAD:
                    costoAdicional += envio.getCosto() * 0.5;
                    break;
            }
        }
        
        envio.setCosto(costoBase + costoAdicional);
        return envio;
    }
    
    private void asignarRepartidorAutomatico(Envio envio) {
        // Buscar primer repartidor disponible
        for (Repartidor repartidor : store.repartidores().values()) {
            if (repartidor.getDisponibilidad() == DriverAvailability.ACTIVO) {
                envio.setRepartidor(repartidor);
                break;
            }
        }
    }
    
    private PaymentResult convertirResultadoPago(PaymentGateway.PaymentResult resultado) {
        switch (resultado) {
            case SUCCESS:
                return PaymentResult.APROBADO;
            case FAILED:
                return PaymentResult.RECHAZADO;
            case PENDING:
                return PaymentResult.RECHAZADO;
            default:
                return PaymentResult.RECHAZADO;
        }
    }
}
