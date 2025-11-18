package co.edu.uniquindio.logistica.adapter;


/**
 * Adapter que adapta el ExternalPaymentService a la interfaz PaymentGateway.
 * Implementa el patrón Adapter para permitir que nuestro sistema interactúe
 * con el sistema de pago externo sin modificar nuestro código existente.
 * 
 * Aplicación del patrón Adapter:
 * - Problema: El sistema de pago externo tiene una interfaz incompatible 
 *   con nuestro dominio (nombres de métodos, tipos de datos)
 * - Propósito: Convertir la interfaz del sistema externo a la interfaz 
 *   que nuestro sistema espera
 * - Solución: Clase adaptadora que implementa nuestra interfaz y delega 
 *   las llamadas al sistema externo con las transformaciones necesarias
 */
public class PaymentGatewayAdapter implements PaymentGateway {
    
    private final ExternalPaymentService externalService;
    
    /**
     * Constructor que recibe el servicio externo a adaptar
     */
    public PaymentGatewayAdapter(ExternalPaymentService externalService) {
        this.externalService = externalService;
    }
    
    @Override
    public PaymentResult processPayment(String merchantId, double amount, String cardNumber) {
        // Adaptación: convertir nuestros parámetros al formato esperado por el sistema externo
        String externalMerchantCode = adaptMerchantId(merchantId);
        String paymentMethod = adaptPaymentMethod(cardNumber);
        
        // Delegar la llamada al sistema externo
        String externalResult = externalService.executeTransaction(
            externalMerchantCode, 
            amount, 
            paymentMethod
        );
        
        // Adaptación: convertir el resultado del sistema externo a nuestro formato
        return adaptPaymentResult(externalResult);
    }
    
    @Override
    public String checkStatus(String transactionId) {
        // Delegar la consulta al sistema externo
        String externalStatus = externalService.queryTransactionStatus(transactionId);
        
        // Adaptar el estado al formato de nuestro sistema
        return adaptStatus(externalStatus);
    }
    
    /**
     * Adapta el ID de comerciante al formato del sistema externo
     */
    private String adaptMerchantId(String merchantId) {
        // El sistema externo espera un código específico
        return "MERCH-LOGIST-001";
    }
    
    /**
     * Adapta el método de pago al formato esperado
     */
    private String adaptPaymentMethod(String cardNumber) {
        return cardNumber != null && !cardNumber.isEmpty() ? "CREDIT_CARD" : "CASH";
    }
    
    /**
     * Convierte el resultado del sistema externo a nuestro enum PaymentResult
     */
    private PaymentResult adaptPaymentResult(String externalResult) {
        if (externalResult.startsWith("TRANSACTION_OK")) {
            return PaymentResult.SUCCESS;
        }
        return PaymentResult.FAILED;
    }
    
    /**
     * Convierte el estado del sistema externo a nuestro formato
     */
    private String adaptStatus(String externalStatus) {
        switch (externalStatus) {
            case "COMPLETED":
                return "COMPLETADO";
            case "FAILED":
                return "FALLIDO";
            default:
                return "DESCONOCIDO";
        }
    }
}
