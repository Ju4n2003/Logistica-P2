package co.edu.uniquindio.logistica.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Interfaz que define el patrón Observer para notificaciones de estado.
 * Permite que múltiples objetos observen los cambios en un sujeto y 
 * reaccionen ante ellos de forma desacoplada.
 * 
 * Aplicación del patrón Observer:
 * - Problema: Necesidad de notificar a múltiples componentes cuando 
 *   cambia el estado de un envío sin acoplarlos directamente
 * - Propósito: Definir una relación uno-a-muchos entre objetos para que 
 *   cuando uno cambie de estado, todos sus dependientes sean notificados
 * - Solución: Interfaz Sujeto que mantiene lista de observadores y los 
 *   notifica cuando ocurren cambios
 */
public interface ShipmentSubject {
    
    /**
     * Registra un nuevo observador que será notificado de los cambios
     */
    void addObserver(ShipmentObserver observer);
    
    /**
     * Elimina un observador de la lista de notificaciones
     */
    void removeObserver(ShipmentObserver observer);
    
    /**
     * Notifica a todos los observadores sobre un cambio de estado
     */
    void notifyObservers(String shipmentId, String oldStatus, String newStatus);
    
    /**
     * Implementación base para facilitar el patrón Observer
     */
    abstract class ShipmentSubjectBase implements ShipmentSubject {
        protected final List<ShipmentObserver> observers = new ArrayList<>();
        
        @Override
        public void addObserver(ShipmentObserver observer) {
            if (!observers.contains(observer)) {
                observers.add(observer);
            }
        }
        
        @Override
        public void removeObserver(ShipmentObserver observer) {
            observers.remove(observer);
        }
        
        @Override
        public void notifyObservers(String shipmentId, String oldStatus, String newStatus) {
            for (ShipmentObserver observer : observers) {
                observer.update(shipmentId, oldStatus, newStatus);
            }
        }
    }
}
