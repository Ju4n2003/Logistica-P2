package co.edu.uniquindio.logistica.adapter;

/**
 * Interfaz que representa un gateway de pago externo.
 * Define el contrato que deben cumplir todos los procesadores de pago.
 */
public interface PaymentGateway {
    
    /**
     * Procesa un pago a través del gateway externo
     * @param merchantId Identificador del comerciante
     * @param amount Monto a procesar
     * @param cardNumber Número de tarjeta (simulado)
     * @return Resultado del procesamiento
     */
    PaymentResult processPayment(String merchantId, double amount, String cardNumber);
    
    /**
     * Verifica el estado de una transacción
     * @param transactionId Identificador de la transacción
     * @return Estado actual de la transacción
     */
    String checkStatus(String transactionId);
    
    /**
     * Resultado del procesamiento de pago
     */
    enum PaymentResult {
        SUCCESS("APROBADO"),
        FAILED("RECHAZADO"),
        PENDING("PENDIENTE");
        
        private final String internalStatus;
        
        PaymentResult(String internalStatus) {
            this.internalStatus = internalStatus;
        }
        
        public String getInternalStatus() {
            return internalStatus;
        }
    }
}
