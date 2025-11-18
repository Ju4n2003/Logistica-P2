package co.edu.uniquindio.logistica.model;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Objects;

public class Envio {
  private String idEnvio;
  private Direccion origen;
  private Direccion destino;
  private double pesoKg;
  private double volumenM3;
  private double costo;
  private ShipmentStatus estado;
  private LocalDateTime fechaCreacion;
  private LocalDateTime fechaEstimadaEntrega;
  private Repartidor repartidor;
  private Usuario usuario;
  private EnumSet<ServiceExtraType> servicios = EnumSet.noneOf(ServiceExtraType.class);

  public String getIdEnvio() {
    return idEnvio;
  }

  public void setIdEnvio(String idEnvio) {
    this.idEnvio = idEnvio;
  }

  public Direccion getOrigen() {
    return origen;
  }

  public void setOrigen(Direccion origen) {
    this.origen = origen;
  }

  public Direccion getDestino() {
    return destino;
  }

  public void setDestino(Direccion destino) {
    this.destino = destino;
  }

  public double getPesoKg() {
    return pesoKg;
  }

  public void setPesoKg(double pesoKg) {
    this.pesoKg = pesoKg;
  }

  public double getVolumenM3() {
    return volumenM3;
  }

  public void setVolumenM3(double volumenM3) {
    this.volumenM3 = volumenM3;
  }

  public double getCosto() {
    return costo;
  }

  public void setCosto(double costo) {
    this.costo = costo;
  }

  public ShipmentStatus getEstado() {
    return estado;
  }

  public void setEstado(ShipmentStatus estado) {
    this.estado = estado;
  }

  public LocalDateTime getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(LocalDateTime fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  public LocalDateTime getFechaEstimadaEntrega() {
    return fechaEstimadaEntrega;
  }

  public void setFechaEstimadaEntrega(LocalDateTime fechaEstimadaEntrega) {
    this.fechaEstimadaEntrega = fechaEstimadaEntrega;
  }

  public Repartidor getRepartidor() {
    return repartidor;
  }

  public void setRepartidor(Repartidor repartidor) {
    this.repartidor = repartidor;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public EnumSet<ServiceExtraType> getServicios() {
    return servicios;
  }

  public void setServicios(EnumSet<ServiceExtraType> servicios) {
    this.servicios = servicios;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Envio envio = (Envio) o;
    return Objects.equals(idEnvio, envio.idEnvio);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idEnvio);
  }
}
