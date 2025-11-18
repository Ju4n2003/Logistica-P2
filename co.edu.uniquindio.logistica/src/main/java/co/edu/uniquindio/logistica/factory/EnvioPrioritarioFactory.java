package co.edu.uniquindio.logistica.factory;

import co.edu.uniquindio.logistica.model.Envio;
import co.edu.uniquindio.logistica.service.impl.SimpleTarifaStrategy;
import co.edu.uniquindio.logistica.service.ITarifaStrategy;

/**
 * Concrete Factory para crear envíos prioritarios.
 * Implementa la lógica específica para envíos con prioridad y entrega rápida.
 */
public class EnvioPrioritarioFactory extends EnvioFactory {
    
    private final ITarifaStrategy tarifaStrategy = new SimpleTarifaStrategy();
    
    @Override
    public Envio crearEnvio(EnvioData datos) {
        Envio envio = new Envio();
        configurarEnvioBase(envio, datos);
        
        // Calcular costo para envío prioritario
        var tarifa = tarifaStrategy.calcular(
            datos.origenId(), 
            datos.destinoId(), 
            datos.pesoKg(), 
            datos.volumenM3(), 
            true
        );
        
        envio.setCosto(tarifa.total());
        envio.setFechaEstimadaEntrega(envio.getFechaCreacion().plusHours(12));
        
        return envio;
    }
}
