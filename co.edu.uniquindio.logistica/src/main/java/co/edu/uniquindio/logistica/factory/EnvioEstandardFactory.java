package co.edu.uniquindio.logistica.factory;

import co.edu.uniquindio.logistica.model.Envio;
import co.edu.uniquindio.logistica.service.impl.SimpleTarifaStrategy;
import co.edu.uniquindio.logistica.service.ITarifaStrategy;

/**
 * Concrete Factory para crear envíos estándar.
 * Implementa la lógica específica para envíos sin prioridad.
 */
public class EnvioEstandardFactory extends EnvioFactory {
    
    private final ITarifaStrategy tarifaStrategy = new SimpleTarifaStrategy();
    
    @Override
    public Envio crearEnvio(EnvioData datos) {
        Envio envio = new Envio();
        configurarEnvioBase(envio, datos);
        
        // Calcular costo para envío estándar (sin prioridad)
        tarifaStrategy.calcular(
            datos.origenId(), 
            datos.destinoId(), 
            datos.pesoKg(), 
            datos.volumenM3(), 
            false
        );
        
        // Por ahora, usar un cálculo simple hasta que se corrija la interfaz
        double costoCalculado = 15000; // Costo base estándar
        envio.setCosto(costoCalculado);
        envio.setFechaEstimadaEntrega(envio.getFechaCreacion().plusDays(3));
        
        return envio;
    }
}
