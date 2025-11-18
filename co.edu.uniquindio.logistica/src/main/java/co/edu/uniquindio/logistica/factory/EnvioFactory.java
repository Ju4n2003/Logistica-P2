package co.edu.uniquindio.logistica.factory;

import co.edu.uniquindio.logistica.model.*;
import co.edu.uniquindio.logistica.repository.InMemoryStore;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

/**
 * Factory Method para la creación de diferentes tipos de envíos.
 * Implementa el patrón creacional Factory Method para centralizar
 * la lógica de creación de envíos según el tipo y prioridad.
 */
public abstract class EnvioFactory {
    
    protected final InMemoryStore store = InMemoryStore.getInstance();
    
    /**
     * Método factory que crea un envío según los parámetros
     * @param datos Datos básicos del envío
     * @return Envío creado con sus configuraciones específicas
     */
    public abstract Envio crearEnvio(EnvioData datos);
    
    /**
     * Configura el envío base con datos comunes
     */
    protected Envio configurarEnvioBase(Envio envio, EnvioData datos) {
        envio.setIdEnvio(store.nextId("ENV"));
        envio.setOrigen(store.direcciones().get(datos.origenId()));
        envio.setDestino(store.direcciones().get(datos.destinoId()));
        envio.setPesoKg(datos.pesoKg());
        envio.setVolumenM3(datos.volumenM3());
        envio.setUsuario(store.usuarios().get(datos.usuarioId()));
        envio.setFechaCreacion(LocalDateTime.now());
        envio.setEstado(ShipmentStatus.SOLICITADO);
        envio.setServicios(EnumSet.copyOf(datos.serviciosAdicionales()));
        return envio;
    }
    
    /**
     * Record para encapsular datos de creación de envíos
     */
    public record EnvioData(
        String origenId,
        String destinoId,
        double pesoKg,
        double volumenM3,
        String usuarioId,
        Set<ServiceExtraType> serviciosAdicionales
    ) {}
}
