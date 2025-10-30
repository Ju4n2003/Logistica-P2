package co.edu.uniquindio.logistica.service.impl;

import co.edu.uniquindio.logistica.dto.PagoDTO;
import co.edu.uniquindio.logistica.model.Pago;
import co.edu.uniquindio.logistica.model.PaymentResult;
import co.edu.uniquindio.logistica.repository.InMemoryStore;
import co.edu.uniquindio.logistica.service.IPaymentService;
import java.time.LocalDateTime;

/** Servicio de pagos simulado que registra pagos en memoria y los marca como aprobados. */
public class PaymentServiceImpl implements IPaymentService {
  private final InMemoryStore store = InMemoryStore.getInstance();

  /**
   * Crea un registro de pago aprobado para el envÃƒÂ­o indicado.
   *
   * @param idEnvio id del envÃƒÂ­o a pagar
   * @param monto monto a cobrar
   * @param metodo mÃƒÂ©todo de pago (p. ej., TARJETA)
   * @return DTO con los datos del pago registrado
   */
  @Override
  public PagoDTO procesarPago(String idEnvio, double monto, String metodo) {
    Pago pago = new Pago();
    pago.setIdPago(store.nextId("PAG"));
    pago.setMonto(monto);
    pago.setFecha(LocalDateTime.now());
    pago.setMetodo(metodo);
    pago.setResultado(PaymentResult.APROBADO);
    store.pagos().put(pago.getIdPago(), pago);
    return new PagoDTO(
        pago.getIdPago(),
        pago.getMonto(),
        pago.getFecha(),
        pago.getMetodo(),
        pago.getResultado().name());
  }
}
