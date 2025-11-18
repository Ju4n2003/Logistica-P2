package co.edu.uniquindio.logistica.adapter;

import java.util.UUID;

/**
 * Implementación simulada de un servicio de pago externo.
 * Representa el sistema de pago de un tercero que necesitamos adaptar.
 */
public class ExternalPaymentService {
    
    private static final String MERCHANT_ID = "MERCH-LOGIST-001";
    
    /**
     * Método del sistema externo que procesa pagos
     * @param merchantCode Código de comerciante
     * @param transactionAmount Monto de la transacción
     * @param paymentMethod Método de pago
     * @return Código de respuesta del sistema externo
     */
    public String executeTransaction(String merchantCode, double transactionAmount, String paymentMethod) {
        // Simulación de procesamiento externo
        System.out.println("Procesando pago externo - Merchant: " + merchantCode + 
                          ", Amount: " + transactionAmount + ", Method: " + paymentMethod);
        
        // Validar que el merchant code sea el esperado
        if (!MERCHANT_ID.equals(merchantCode)) {
            return "INVALID_MERCHANT";
        }
        
        // Simulación de aprobación/rechazo aleatorio
        if (transactionAmount > 100000) {
            return "TRANSACTION_DECLINED";
        }
        
        return "TRANSACTION_OK_" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * Consulta el estado de una transacción en el sistema externo
     * @param transactionCode Código de transacción
     * @return Estado en formato del sistema externo
     */
    public String queryTransactionStatus(String transactionCode) {
        if (transactionCode.startsWith("TRANSACTION_OK")) {
            return "COMPLETED";
        }
        return "FAILED";
    }
}
