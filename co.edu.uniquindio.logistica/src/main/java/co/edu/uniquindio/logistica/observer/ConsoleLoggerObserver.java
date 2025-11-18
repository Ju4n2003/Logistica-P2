package co.edu.uniquindio.logistica.observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Observador que registra en consola los cambios de estado de los envíos.
 * Útil para debugging y auditoría del sistema.
 * 
 * Aplicación del patrón Observer:
 * - Problema: Necesidad de registrar todos los cambios de estado 
 *   para auditoría y diagnóstico sin acoplar el código de logging
 * - Propósito: Proporcionar una implementación de observador que 
 *   capture y registre eventos de cambio de estado
 * - Solución: Clase concreta que implementa ShipmentObserver y 
 *   escribe en consola los detalles del cambio
 */
public class ConsoleLoggerObserver implements ShipmentObserver {
    
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public void update(String shipmentId, String oldStatus, String newStatus) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.printf("[%s] CAMBIO DE ESTADO - Envío: %s | %s -> %s%n",
                         timestamp, shipmentId, oldStatus, newStatus);
    }
}
