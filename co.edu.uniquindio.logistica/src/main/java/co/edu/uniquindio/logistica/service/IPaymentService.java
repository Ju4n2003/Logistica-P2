package co.edu.uniquindio.logistica.service;

import co.edu.uniquindio.logistica.dto.PagoDTO;

public interface IPaymentService {
  PagoDTO procesarPago(String idEnvio, double monto, String metodo);
}
