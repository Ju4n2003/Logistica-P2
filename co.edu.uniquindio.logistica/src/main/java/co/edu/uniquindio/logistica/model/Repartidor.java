package co.edu.uniquindio.logistica.model;

import java.util.Objects;

public class Repartidor {
  private String idRepartidor;
  private String nombre;
  private String documento;
  private String telefono;
  private DriverAvailability disponibilidad;
  private String zonaCobertura;

  public Repartidor() {}

  public Repartidor(
      String idRepartidor,
      String nombre,
      String documento,
      String telefono,
      DriverAvailability disponibilidad,
      String zonaCobertura) {
    this.idRepartidor = idRepartidor;
    this.nombre = nombre;
    this.documento = documento;
    this.telefono = telefono;
    this.disponibilidad = disponibilidad;
    this.zonaCobertura = zonaCobertura;
  }

  public String getIdRepartidor() {
    return idRepartidor;
  }

  public void setIdRepartidor(String idRepartidor) {
    this.idRepartidor = idRepartidor;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDocumento() {
    return documento;
  }

  public void setDocumento(String documento) {
    this.documento = documento;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public DriverAvailability getDisponibilidad() {
    return disponibilidad;
  }

  public void setDisponibilidad(DriverAvailability disponibilidad) {
    this.disponibilidad = disponibilidad;
  }

  public String getZonaCobertura() {
    return zonaCobertura;
  }

  public void setZonaCobertura(String zonaCobertura) {
    this.zonaCobertura = zonaCobertura;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Repartidor that = (Repartidor) o;
    return Objects.equals(idRepartidor, that.idRepartidor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idRepartidor);
  }
}
