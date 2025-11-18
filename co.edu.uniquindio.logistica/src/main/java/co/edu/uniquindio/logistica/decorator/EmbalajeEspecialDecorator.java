package co.edu.uniquindio.logistica.decorator;

/**
 * Decorador que añade embalaje especial al envío.
 * Proporciona protección adicional para artículos frágiles con costo fijo.
 */
public class EmbalajeEspecialDecorator extends EnvioDecorator {
    
    private static final double COSTO_EMBALAJE = 8000;
    private final String tipoEmbalaje;
    
    public EmbalajeEspecialDecorator(EnvioService envioService, String tipoEmbalaje) {
        super(envioService);
        this.tipoEmbalaje = tipoEmbalaje;
    }
    
    @Override
    public double calcularCostoBase() {
        double costoBase = super.calcularCostoBase();
        return costoBase + COSTO_EMBALAJE;
    }
    
    @Override
    public String getDescripcion() {
        return super.getDescripcion() + String.format(" + Embalaje Especial (%s)", tipoEmbalaje);
    }
    
    @Override
    public int getTiempoEntregaHoras() {
        return super.getTiempoEntregaHoras(); // El embalaje no afecta el tiempo
    }
    
    public String getTipoEmbalaje() {
        return tipoEmbalaje;
    }
    
    public double getCostoEmbalaje() {
        return COSTO_EMBALAJE;
    }
}
