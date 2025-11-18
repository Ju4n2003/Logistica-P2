package co.edu.uniquindio.logistica.decorator;

/**
 * Implementación concreta base del servicio de envío.
 * Representa un envío estándar sin servicios adicionales.
 * Es el componente concreto que será decorado.
 */
public class EnvioBasico implements EnvioService {
    
    private final double pesoKg;
    private final double volumenM3;
    private final double distanciaKm;
    
    public EnvioBasico(double pesoKg, double volumenM3, double distanciaKm) {
        this.pesoKg = pesoKg;
        this.volumenM3 = volumenM3;
        this.distanciaKm = distanciaKm;
    }
    
    @Override
    public double calcularCostoBase() {
        // Cálculo básico: fijo + peso + volumen + distancia
        double costoFijo = 5000;
        double costoPeso = pesoKg * 2000;
        double costoVolumen = volumenM3 * 10000;
        double costoDistancia = distanciaKm * 100;
        
        return costoFijo + costoPeso + costoVolumen + costoDistancia;
    }
    
    @Override
    public String getDescripcion() {
        return String.format("Envío básico (%.1f kg, %.2f m³, %.1f km)", 
                           pesoKg, volumenM3, distanciaKm);
    }
    
    @Override
    public int getTiempoEntregaHoras() {
        // Tiempo base según distancia
        if (distanciaKm <= 10) return 2;
        if (distanciaKm <= 50) return 6;
        if (distanciaKm <= 100) return 12;
        return 24;
    }
    
    // Getters para que los decorators accedan a los datos
    public double getPesoKg() { return pesoKg; }
    public double getVolumenM3() { return volumenM3; }
    public double getDistanciaKm() { return distanciaKm; }
}
