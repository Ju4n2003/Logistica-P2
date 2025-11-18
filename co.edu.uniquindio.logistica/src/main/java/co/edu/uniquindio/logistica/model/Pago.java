package co.edu.uniquindio.logistica.model;

import java.time.LocalDateTime;

public class Pago {
  private String idPago;
  private double monto;
  private LocalDateTime fecha;
  private String metodo;
  private PaymentResult resultado;

  public String getIdPago() {
    return idPago;
  }

  public void setIdPago(String idPago) {
    this.idPago = idPago;
  }

  public double getMonto() {
    return monto;
  }

  public void setMonto(double monto) {
    this.monto = monto;
  }

  public LocalDateTime getFecha() {
    return fecha;
  }

  public void setFecha(LocalDateTime fecha) {
    this.fecha = fecha;
  }

  public String getMetodo() {
    return metodo;
  }

  public void setMetodo(String metodo) {
    this.metodo = metodo;
  }

  public PaymentResult getResultado() {
    return resultado;
  }

  public void setResultado(PaymentResult resultado) {
    this.resultado = resultado;
  }
}
