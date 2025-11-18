package co.edu.uniquindio.logistica.observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Observador que mantiene un historial de notificaciones de cambios de estado.
 * Útil para análisis y reportes de actividad del sistema.
 * 
 * Aplicación del patrón Observer:
 * - Problema: Necesidad de mantener un registro estructurado de 
 *   todos los cambios de estado para reportes y análisis
 * - Propósito: Proporcionar una implementación de observador que 
 *   almacene eventos de cambio de estado en memoria
 * - Solución: Clase que implementa ShipmentObserver y mantiene 
 *   una lista de eventos con timestamp
 */
public class HistoryTrackerObserver implements ShipmentObserver {
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final List<ShipmentStatusChangeEvent> eventHistory = new ArrayList<>();
    
    @Override
    public void update(String shipmentId, String oldStatus, String newStatus) {
        ShipmentStatusChangeEvent event = new ShipmentStatusChangeEvent(
            shipmentId, oldStatus, newStatus, LocalDateTime.now()
        );
        eventHistory.add(event);
    }
    
    /**
     * Obtiene todo el historial de eventos
     */
    public List<ShipmentStatusChangeEvent> getEventHistory() {
        return new ArrayList<>(eventHistory);
    }
    
    /**
     * Obtiene los eventos para un envío específico
     */
    public List<ShipmentStatusChangeEvent> getEventsForShipment(String shipmentId) {
        return eventHistory.stream()
            .filter(event -> event.shipmentId().equals(shipmentId))
            .toList();
    }
    
    /**
     * Limpia el historial de eventos
     */
    public void clearHistory() {
        eventHistory.clear();
    }
    
    /**
     * Clase que representa un evento de cambio de estado
     */
    public static class ShipmentStatusChangeEvent {
        private final String shipmentId;
        private final String oldStatus;
        private final String newStatus;
        private final LocalDateTime timestamp;
        
        public ShipmentStatusChangeEvent(String shipmentId, String oldStatus, String newStatus, LocalDateTime timestamp) {
            this.shipmentId = shipmentId;
            this.oldStatus = oldStatus;
            this.newStatus = newStatus;
            this.timestamp = timestamp;
        }
        
        public String shipmentId() { return shipmentId; }
        public String oldStatus() { return oldStatus; }
        public String newStatus() { return newStatus; }
        public LocalDateTime timestamp() { return timestamp; }
        
        @Override
        public String toString() {
            return String.format("[%s] Envío %s: %s -> %s",
                timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), 
                shipmentId, oldStatus, newStatus);
        }
    }
}
