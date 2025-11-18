package co.edu.uniquindio.logistica.model;

import java.util.Objects;

public class Direccion {
  private String idDireccion;
  private String alias;
  private String calle;
  private String ciudad;

  public Direccion() {}

  public Direccion(String idDireccion, String alias, String calle, String ciudad) {
    this.idDireccion = idDireccion;
    this.alias = alias;
    this.calle = calle;
    this.ciudad = ciudad;
  }

  public String getIdDireccion() {
    return idDireccion;
  }

  public void setIdDireccion(String idDireccion) {
    this.idDireccion = idDireccion;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getCalle() {
    return calle;
  }

  public void setCalle(String calle) {
    this.calle = calle;
  }

  public String getCiudad() {
    return ciudad;
  }

  public void setCiudad(String ciudad) {
    this.ciudad = ciudad;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Direccion that = (Direccion) o;
    return Objects.equals(idDireccion, that.idDireccion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idDireccion);
  }
}
