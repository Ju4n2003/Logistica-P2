package co.edu.uniquindio.logistica.dto;

import java.time.LocalDateTime;

/** Informacion de un pago realizado (monto, fecha, metodo y resultado). */
public record PagoDTO(
    String idPago, double monto, LocalDateTime fecha, String metodo, String resultado) {}
