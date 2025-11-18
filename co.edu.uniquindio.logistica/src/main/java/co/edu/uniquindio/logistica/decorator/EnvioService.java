package co.edu.uniquindio.logistica.decorator;

/**
 * Interfaz base para servicios de envío que será decorada.
 * Define las operaciones básicas que todo envío debe tener.
 */
public interface EnvioService {
    
    /**
     * Calcula el costo base del envío
     * @return Costo base del envío
     */
    double calcularCostoBase();
    
    /**
     * Obtiene la descripción del servicio de envío
     * @return Descripción del envío
     */
    String getDescripcion();
    
    /**
     * Obtiene el tiempo estimado de entrega
     * @return Tiempo en horas
     */
    int getTiempoEntregaHoras();
}
