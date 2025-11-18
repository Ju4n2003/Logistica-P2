package co.edu.uniquindio.logistica.observer;

import co.edu.uniquindio.logistica.model.Envio;
import co.edu.uniquindio.logistica.model.ShipmentStatus;
import co.edu.uniquindio.logistica.repository.InMemoryStore;

/**
 * Implementación concreta del sujeto observable para envíos.
 * Esta clase extiende la funcionalidad básica de los envíos para 
 * soportar notificaciones a observadores cuando cambia su estado.
 * 
 * Aplicación del patrón Observer:
 * - Problema: Los envíos necesitan notificar a múltiples componentes 
 *   sobre cambios de estado (UI, servicios de email, logs, etc.)
 * - Propósito: Permitir que los envíos actúen como sujetos observables 
 *   sin modificar su estructura original
 * - Solución: Clase que envuelve un envío y añade capacidades 
 *   de observación usando composición
 */
public class ObservableShipment extends ShipmentSubject.ShipmentSubjectBase {
    
    private final Envio envio;
    private final InMemoryStore store;
    
    public ObservableShipment(Envio envio, InMemoryStore store) {
        this.envio = envio;
        this.store = store;
    }
    
    /**
     * Actualiza el estado del envío y notifica a los observadores
     */
    public void updateStatus(ShipmentStatus newStatus) {
        ShipmentStatus oldStatus = envio.getEstado();
        envio.setEstado(newStatus);
        
        // Actualizar en el almacenamiento
        store.envios().put(envio.getIdEnvio(), envio);
        
        // Notificar a todos los observadores
        notifyObservers(envio.getIdEnvio(), oldStatus.toString(), newStatus.toString());
    }
    
    /**
     * Obtiene el envío envuelto
     */
    public Envio getEnvio() {
        return envio;
    }
    
    /**
     * Obtiene el estado actual del envío
     */
    public ShipmentStatus getStatus() {
        return envio.getEstado();
    }
}
