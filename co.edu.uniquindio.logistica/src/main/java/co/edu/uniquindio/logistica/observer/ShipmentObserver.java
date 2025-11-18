package co.edu.uniquindio.logistica.observer;

/**
 * Interfaz que define el observador en el patrón Observer.
 * Los observadores implementan esta interfaz para recibir notificaciones
 * sobre cambios de estado en los envíos.
 * 
 * Aplicación del patrón Observer:
 * - Problema: Componentes que necesitan reaccionar a cambios de estado 
 *   sin estar acoplados directamente al objeto que cambia
 * - Propósito: Permitir que un objeto notifique a otros objetos sobre 
 *   cambios en su estado sin necesidad de conocerlos directamente
 * - Solución: Interfaz Observer que define el método de actualización 
 *   que todos los observadores deben implementar
 */
public interface ShipmentObserver {
    
    /**
     * Método llamado cuando el estado de un envío cambia
     * 
     * @param shipmentId Identificador del envío que cambió de estado
     * @param oldStatus Estado anterior del envío
     * @param newStatus Nuevo estado del envío
     */
    void update(String shipmentId, String oldStatus, String newStatus);
}
