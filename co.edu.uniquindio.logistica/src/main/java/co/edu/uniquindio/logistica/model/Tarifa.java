package co.edu.uniquindio.logistica.model;

public class Tarifa {
  private double base;
  private double peso;
  private double volumen;
  private double prioridad;
  private double recargos;

  public double getBase() {
    return base;
  }

  public void setBase(double base) {
    this.base = base;
  }

  public double getPeso() {
    return peso;
  }

  public void setPeso(double peso) {
    this.peso = peso;
  }

  public double getVolumen() {
    return volumen;
  }

  public void setVolumen(double volumen) {
    this.volumen = volumen;
  }

  public double getPrioridad() {
    return prioridad;
  }

  public void setPrioridad(double prioridad) {
    this.prioridad = prioridad;
  }

  public double getRecargos() {
    return recargos;
  }

  public void setRecargos(double recargos) {
    this.recargos = recargos;
  }

  public double total() {
    return base + peso + volumen + prioridad + recargos;
  }
}
