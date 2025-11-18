package co.edu.uniquindio.logistica.decorator;

/**
 * Decorador que añade entrega rápida al envío.
 * Reduce el tiempo de entrega a la mitad con un costo adicional.
 */
public class EntregaRapidaDecorator extends EnvioDecorator {
    
    private static final double RECARGO_RAPIDO = 0.5; // 50% de recargo
    
    public EntregaRapidaDecorator(EnvioService envioService) {
        super(envioService);
    }
    
    @Override
    public double calcularCostoBase() {
        double costoBase = super.calcularCostoBase();
        double recargo = costoBase * RECARGO_RAPIDO;
        return costoBase + recargo;
    }
    
    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Entrega Rápida";
    }
    
    @Override
    public int getTiempoEntregaHoras() {
        int tiempoBase = super.getTiempoEntregaHoras();
        return Math.max(1, tiempoBase / 2); // Mínimo 1 hora
    }
    
    public double getRecargo() {
        return super.calcularCostoBase() * RECARGO_RAPIDO;
    }
}
