package co.edu.uniquindio.logistica.command;

import co.edu.uniquindio.logistica.model.Envio;
import co.edu.uniquindio.logistica.model.ShipmentStatus;
import co.edu.uniquindio.logistica.repository.InMemoryStore;

/**
 * Comando concreto para actualizar el estado de un envío.
 * Implementa el patrón Command para encapsular la lógica de actualización.
 * 
 * Aplicación del patrón Command:
 * - Problema: Necesidad de encapsular cambios de estado como acciones 
 *   reversibles y auditables
 * - Propósito: Permitir que las actualizaciones de estado sean tratadas 
 *   como comandos con capacidad de deshacer y registro
 * - Solución: Clase concreta que implementa AdminCommand y 
 *   encapsula toda la lógica de actualización de estado
 */
public class UpdateShipmentStatusCommand implements AdminCommand {
    
    private final InMemoryStore store;
    private final String shipmentId;
    private final ShipmentStatus newStatus;
    
    private ShipmentStatus oldStatus; // Guardar estado anterior para undo
    
    public UpdateShipmentStatusCommand(InMemoryStore store, String shipmentId, ShipmentStatus newStatus) {
        this.store = store;
        this.shipmentId = shipmentId;
        this.newStatus = newStatus;
    }
    
    @Override
    public CommandResult execute() {
        try {
            Envio envio = store.envios().get(shipmentId);
            if (envio == null) {
                return new CommandResult(false, "Envío no encontrado: " + shipmentId);
            }
            
            // Guardar estado anterior
            oldStatus = envio.getEstado();
            
            // Actualizar estado
            envio.setEstado(newStatus);
            store.envios().put(shipmentId, envio);
            
            return new CommandResult(true, String.format(
                "Envío %s actualizado: %s -> %s", 
                shipmentId, oldStatus, newStatus
            ));
            
        } catch (Exception e) {
            return new CommandResult(false, "Error al actualizar estado: " + e.getMessage());
        }
    }
    
    @Override
    public CommandResult undo() {
        if (oldStatus == null) {
            return new CommandResult(false, "No se puede deshacer: el comando no fue ejecutado");
        }
        
        try {
            Envio envio = store.envios().get(shipmentId);
            if (envio == null) {
                return new CommandResult(false, "Envío no encontrado para deshacer");
            }
            
            // Restaurar estado anterior
            envio.setEstado(oldStatus);
            store.envios().put(shipmentId, envio);
            
            return new CommandResult(true, String.format(
                "Estado restaurado para envío %s: %s", 
                shipmentId, oldStatus
            ));
            
        } catch (Exception e) {
            return new CommandResult(false, "Error al deshacer actualización: " + e.getMessage());
        }
    }
    
    @Override
    public String getDescription() {
        return String.format("Actualizar estado envío %s a %s", shipmentId, newStatus);
    }
}
