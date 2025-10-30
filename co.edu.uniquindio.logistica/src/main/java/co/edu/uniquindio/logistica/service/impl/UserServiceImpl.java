package co.edu.uniquindio.logistica.service.impl;

import co.edu.uniquindio.logistica.dto.*;
import co.edu.uniquindio.logistica.model.*;
import co.edu.uniquindio.logistica.repository.InMemoryStore;
import co.edu.uniquindio.logistica.service.IPaymentService;
import co.edu.uniquindio.logistica.service.ITarifaStrategy;
import co.edu.uniquindio.logistica.service.IUserService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ImplementaciÃƒÂ³n en memoria de casos de uso del usuario. Gestiona registro/login,
 * perfil/direcciones, cotizaciÃƒÂ³n, creaciÃƒÂ³n/cancelaciÃƒÂ³n de envÃƒÂ­os, pagos simulados, tracking e
 * historial filtrado.
 */
public class UserServiceImpl implements IUserService {

  private final InMemoryStore store = InMemoryStore.getInstance();
  private final ITarifaStrategy tarifaStrategy = new SimpleTarifaStrategy();
  private final IPaymentService paymentService = new PaymentServiceImpl();

  @Override
  public UsuarioDTO register(UsuarioDTO usuario, String password) {
    String id = store.nextId("USR");
    Usuario u = new Usuario(id, usuario.nombreCompleto(), usuario.correo(), usuario.telefono());
    store.usuarios().put(id, u);
    store.credentials().put(usuario.correo(), password);
    return new UsuarioDTO(id, u.getNombreCompleto(), u.getCorreo(), u.getTelefono());
  }

  @Override
  public UsuarioDTO login(String correo, String password) {
    String saved = store.credentials().get(correo);
    if (saved == null || !saved.equals(password)) return null;
    return store.usuarios().values().stream()
        .filter(u -> correo.equals(u.getCorreo()))
        .findFirst()
        .map(
            u ->
                new UsuarioDTO(
                    u.getIdUsuario(), u.getNombreCompleto(), u.getCorreo(), u.getTelefono()))
        .orElse(null);
  }

  @Override
  public UsuarioDTO updateProfile(UsuarioDTO usuario) {
    Usuario u = store.usuarios().get(usuario.idUsuario());
    if (u == null) return null;
    u.setNombreCompleto(usuario.nombreCompleto());
    u.setCorreo(usuario.correo());
    u.setTelefono(usuario.telefono());
    return new UsuarioDTO(u.getIdUsuario(), u.getNombreCompleto(), u.getCorreo(), u.getTelefono());
  }

  @Override
  public DireccionDTO addDireccion(String idUsuario, DireccionDTO direccion) {
    Usuario u = store.usuarios().get(idUsuario);
    if (u == null) return null;
    String id = store.nextId("DIR");
    Direccion d = new Direccion(id, direccion.alias(), direccion.calle(), direccion.ciudad());
    store.direcciones().put(id, d);
    u.getDirecciones().add(d);
    return new DireccionDTO(id, d.getAlias(), d.getCalle(), d.getCiudad());
  }

  @Override
  public void removeDireccion(String idUsuario, String idDireccion) {
    Usuario u = store.usuarios().get(idUsuario);
    if (u == null) return;
    u.getDirecciones().removeIf(d -> idDireccion.equals(d.getIdDireccion()));
    store.direcciones().remove(idDireccion);
  }

  @Override
  public TarifaDTO quote(
      String origenId, String destinoId, double pesoKg, double volumenM3, boolean prioridad) {
    return tarifaStrategy.calcular(origenId, destinoId, pesoKg, volumenM3, prioridad);
  }

  @Override
  public EnvioDTO createEnvio(EnvioDTO dto) {
    Envio e = new Envio();
    String id = store.nextId("ENV");
    e.setIdEnvio(id);
    e.setOrigen(store.direcciones().get(dto.origenId()));
    e.setDestino(store.direcciones().get(dto.destinoId()));
    e.setPesoKg(dto.pesoKg());
    e.setVolumenM3(dto.volumenM3());
    e.setCosto(dto.costo());
    e.setEstado(ShipmentStatus.SOLICITADO);
    e.setFechaCreacion(LocalDateTime.now());
    e.setUsuario(store.usuarios().get(dto.usuarioId()));
    store.envios().put(id, e);
    return new EnvioDTO(
        e.getIdEnvio(),
        e.getOrigen() != null ? e.getOrigen().getIdDireccion() : null,
        e.getDestino() != null ? e.getDestino().getIdDireccion() : null,
        e.getPesoKg(),
        e.getVolumenM3(),
        e.getCosto(),
        e.getEstado().name(),
        e.getFechaCreacion(),
        e.getFechaEstimadaEntrega(),
        e.getRepartidor() != null ? e.getRepartidor().getIdRepartidor() : null,
        e.getUsuario() != null ? e.getUsuario().getIdUsuario() : null,
        Set.of());
  }

  @Override
  public void cancelEnvio(String idEnvio) {
    Envio e = store.envios().get(idEnvio);
    if (e != null && e.getEstado() == ShipmentStatus.SOLICITADO) {
      store.envios().remove(idEnvio);
    }
  }

  @Override
  public PagoDTO pay(String idEnvio, String metodo) {
    Envio e = store.envios().get(idEnvio);
    if (e == null) return null;
    return paymentService.procesarPago(idEnvio, e.getCosto(), metodo);
  }

  @Override
  public EnvioDTO track(String idEnvio) {
    Envio e = store.envios().get(idEnvio);
    if (e == null) return null;
    return new EnvioDTO(
        e.getIdEnvio(),
        e.getOrigen() != null ? e.getOrigen().getIdDireccion() : null,
        e.getDestino() != null ? e.getDestino().getIdDireccion() : null,
        e.getPesoKg(),
        e.getVolumenM3(),
        e.getCosto(),
        e.getEstado().name(),
        e.getFechaCreacion(),
        e.getFechaEstimadaEntrega(),
        e.getRepartidor() != null ? e.getRepartidor().getIdRepartidor() : null,
        e.getUsuario() != null ? e.getUsuario().getIdUsuario() : null,
        Set.of());
  }

  @Override
  public List<EnvioDTO> historial(String idUsuario, String estado, String desde, String hasta) {
    ShipmentStatus filterStatus = estado != null ? ShipmentStatus.valueOf(estado) : null;
    LocalDate from = desde != null && !desde.isBlank() ? LocalDate.parse(desde) : null;
    LocalDate to = hasta != null && !hasta.isBlank() ? LocalDate.parse(hasta) : null;
    return store.envios().values().stream()
        .filter(e -> e.getUsuario() != null && idUsuario.equals(e.getUsuario().getIdUsuario()))
        .filter(e -> filterStatus == null || e.getEstado() == filterStatus)
        .filter(
            e -> {
              if (from == null && to == null) return true;
              LocalDate d =
                  e.getFechaCreacion() != null ? e.getFechaCreacion().toLocalDate() : null;
              if (d == null) return false;
              boolean ok = true;
              if (from != null) ok &= !d.isBefore(from);
              if (to != null) ok &= !d.isAfter(to);
              return ok;
            })
        .map(
            e ->
                new EnvioDTO(
                    e.getIdEnvio(),
                    e.getOrigen() != null ? e.getOrigen().getIdDireccion() : null,
                    e.getDestino() != null ? e.getDestino().getIdDireccion() : null,
                    e.getPesoKg(),
                    e.getVolumenM3(),
                    e.getCosto(),
                    e.getEstado().name(),
                    e.getFechaCreacion(),
                    e.getFechaEstimadaEntrega(),
                    e.getRepartidor() != null ? e.getRepartidor().getIdRepartidor() : null,
                    e.getUsuario() != null ? e.getUsuario().getIdUsuario() : null,
                    Set.of()))
        .collect(Collectors.toCollection(ArrayList::new));
  }
}
