package co.edu.uniquindio.logistica.decorator;

/**
 * Clase base abstracta para los decoradores de envío.
 * Implementa el patrón Decorator manteniendo una referencia 
 * al componente que decora y delegando las operaciones básicas.
 * 
 * Aplicación del patrón Decorator:
 * - Problema: Necesidad de agregar responsabilidades adicionales 
 *   (servicios extra) a los envíos de forma dinámica
 * - Propósito: Permitir la adición de funcionalidades a objetos 
 *   sin modificar su estructura o crear subclases complejas
 * - Solución: Clase decoradora que envuelve al componente original 
 *   y añade nuevas responsabilidades
 */
public abstract class EnvioDecorator implements EnvioService {
    
    /**
     * Referencia al componente que está siendo decorado
     */
    protected final EnvioService envioService;
    
    /**
     * Constructor que recibe el componente a decorar
     * @param envioService Servicio de envío a decorar
     */
    public EnvioDecorator(EnvioService envioService) {
        this.envioService = envioService;
    }
    
    /**
     * Delega el cálculo del costo base al componente decorado
     */
    @Override
    public double calcularCostoBase() {
        return envioService.calcularCostoBase();
    }
    
    /**
     * Delega la descripción base al componente decorado
     */
    @Override
    public String getDescripcion() {
        return envioService.getDescripcion();
    }
    
    /**
     * Delega el tiempo de entrega al componente decorado
     */
    @Override
    public int getTiempoEntregaHoras() {
        return envioService.getTiempoEntregaHoras();
    }
}
