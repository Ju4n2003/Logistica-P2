package co.edu.uniquindio.logistica.decorator;

/**
 * Decorador que añade seguro al envío.
 * Agrega protección contra pérdida o daño con un costo adicional.
 */
public class SeguroDecorator extends EnvioDecorator {
    
    private final double valorDeclarado;
    private static final double PORCENTAJE_SEGURO = 0.02;
    
    public SeguroDecorator(EnvioService envioService, double valorDeclarado) {
        super(envioService);
        this.valorDeclarado = valorDeclarado;
    }
    
    @Override
    public double calcularCostoBase() {
        double costoBase = super.calcularCostoBase();
        double costoSeguro = valorDeclarado * PORCENTAJE_SEGURO;
        return costoBase + costoSeguro;
    }
    
    @Override
    public String getDescripcion() {
        return super.getDescripcion() + String.format(" + Seguro ($%.2f)", valorDeclarado);
    }
    
    @Override
    public int getTiempoEntregaHoras() {
        return super.getTiempoEntregaHoras(); // El seguro no afecta el tiempo
    }
    
    public double getValorDeclarado() {
        return valorDeclarado;
    }
    
    public double getCostoSeguro() {
        return valorDeclarado * PORCENTAJE_SEGURO;
    }
}
