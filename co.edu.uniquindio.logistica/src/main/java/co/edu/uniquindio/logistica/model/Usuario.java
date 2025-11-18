package co.edu.uniquindio.logistica.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Usuario {
  private String idUsuario;
  private String nombreCompleto;
  private String correo;
  private String telefono;
  private List<Direccion> direcciones = new ArrayList<>();

  public Usuario() {}

  public Usuario(String idUsuario, String nombreCompleto, String correo, String telefono) {
    this.idUsuario = idUsuario;
    this.nombreCompleto = nombreCompleto;
    this.correo = correo;
    this.telefono = telefono;
  }

  public String getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(String idUsuario) {
    this.idUsuario = idUsuario;
  }

  public String getNombreCompleto() {
    return nombreCompleto;
  }

  public void setNombreCompleto(String nombreCompleto) {
    this.nombreCompleto = nombreCompleto;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public List<Direccion> getDirecciones() {
    return direcciones;
  }

  public void setDirecciones(List<Direccion> direcciones) {
    this.direcciones = direcciones;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Usuario usuario = (Usuario) o;
    return Objects.equals(idUsuario, usuario.idUsuario);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idUsuario);
  }
}
