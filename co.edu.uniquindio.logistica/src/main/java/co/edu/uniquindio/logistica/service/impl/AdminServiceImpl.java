package co.edu.uniquindio.logistica.service.impl;

import co.edu.uniquindio.logistica.dto.*;
import co.edu.uniquindio.logistica.model.*;
import co.edu.uniquindio.logistica.repository.InMemoryStore;
import co.edu.uniquindio.logistica.service.IAdminService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ImplementaciÃ³n en memoria de casos de uso de administraciÃ³n y operaciones. Gestiona usuarios y
 * repartidores, y operaciones sobre envÃ­os (asignaciÃ³n, estado, incidencias).
 */
public class AdminServiceImpl implements IAdminService {

  private final InMemoryStore store = InMemoryStore.getInstance();

  /** {@inheritDoc} */
  @Override
  public UsuarioDTO createUsuario(UsuarioDTO usuario) {
    String id = store.nextId("USR");
    Usuario u = new Usuario(id, usuario.nombreCompleto(), usuario.correo(), usuario.telefono());
    store.usuarios().put(id, u);
    return new UsuarioDTO(id, u.getNombreCompleto(), u.getCorreo(), u.getTelefono());
  }

  /** {@inheritDoc} */
  @Override
  public UsuarioDTO updateUsuario(UsuarioDTO usuario) {
    Usuario u = store.usuarios().get(usuario.idUsuario());
    if (u == null) return null;
    u.setNombreCompleto(usuario.nombreCompleto());
    u.setCorreo(usuario.correo());
    u.setTelefono(usuario.telefono());
    return new UsuarioDTO(u.getIdUsuario(), u.getNombreCompleto(), u.getCorreo(), u.getTelefono());
  }

  /** {@inheritDoc} */
  @Override
  public void deleteUsuario(String idUsuario) {
    store.usuarios().remove(idUsuario);
  }

  /** {@inheritDoc} */
  @Override
  public List<UsuarioDTO> listUsuarios() {
    return store.usuarios().values().stream()
        .map(
            u ->
                new UsuarioDTO(
                    u.getIdUsuario(), u.getNombreCompleto(), u.getCorreo(), u.getTelefono()))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  /** {@inheritDoc} */
  @Override
  public RepartidorDTO createRepartidor(RepartidorDTO dto) {
    String id = store.nextId("DRI");
    Repartidor r =
        new Repartidor(
            id,
            dto.nombre(),
            dto.documento(),
            dto.telefono(),
            DriverAvailability.valueOf(dto.disponibilidad()),
            dto.zonaCobertura());
    store.repartidores().put(id, r);
    return new RepartidorDTO(
        id,
        r.getNombre(),
        r.getDocumento(),
        r.getTelefono(),
        r.getDisponibilidad().name(),
        r.getZonaCobertura());
  }

  /** {@inheritDoc} */
  @Override
  public RepartidorDTO updateRepartidor(RepartidorDTO dto) {
    Repartidor r = store.repartidores().get(dto.idRepartidor());
    if (r == null) return null;
    r.setNombre(dto.nombre());
    r.setDocumento(dto.documento());
    r.setTelefono(dto.telefono());
    r.setDisponibilidad(DriverAvailability.valueOf(dto.disponibilidad()));
    r.setZonaCobertura(dto.zonaCobertura());
    return new RepartidorDTO(
        r.getIdRepartidor(),
        r.getNombre(),
        r.getDocumento(),
        r.getTelefono(),
        r.getDisponibilidad().name(),
        r.getZonaCobertura());
  }

  /** {@inheritDoc} */
  @Override
  public void deleteRepartidor(String idRepartidor) {
    store.repartidores().remove(idRepartidor);
  }

  /** {@inheritDoc} */
  @Override
  public List<RepartidorDTO> listRepartidores() {
    return store.repartidores().values().stream()
        .map(
            r ->
                new RepartidorDTO(
                    r.getIdRepartidor(),
                    r.getNombre(),
                    r.getDocumento(),
                    r.getTelefono(),
                    r.getDisponibilidad().name(),
                    r.getZonaCobertura()))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  /** {@inheritDoc} */
  @Override
  public EnvioDTO assignEnvio(String idEnvio, String idRepartidor) {
    Envio e = store.envios().get(idEnvio);
    Repartidor r = store.repartidores().get(idRepartidor);
    if (e == null || r == null) return null;
    e.setRepartidor(r);
    e.setEstado(ShipmentStatus.EN_RUTA);
    if (e.getFechaEstimadaEntrega() == null) {
      e.setFechaEstimadaEntrega(LocalDateTime.now().plusHours(4));
    }
    return mapEnvio(e);
  }

  /** {@inheritDoc} */
  @Override
  public EnvioDTO reassignEnvio(String idEnvio, String idRepartidor) {
    return assignEnvio(idEnvio, idRepartidor);
  }

  /** {@inheritDoc} */
  @Override
  public EnvioDTO changeEstado(String idEnvio, String estado) {
    Envio e = store.envios().get(idEnvio);
    if (e == null) return null;
    e.setEstado(ShipmentStatus.valueOf(estado));
    if (e.getEstado() == ShipmentStatus.EN_RUTA && e.getFechaEstimadaEntrega() == null) {
      e.setFechaEstimadaEntrega(LocalDateTime.now().plusHours(4));
    } else if (e.getEstado() == ShipmentStatus.ENTREGADO && e.getFechaEstimadaEntrega() == null) {
      // Registrar momento de entrega si no existe
      e.setFechaEstimadaEntrega(LocalDateTime.now());
    }
    return mapEnvio(e);
  }

  /** {@inheritDoc} */
  @Override
  public void registrarIncidencia(String idEnvio, String descripcion, String zona) {
    // Stub: almacenarÃ­amos en un repositorio de incidencias, por ahora solo cambiamos estado
    Envio e = store.envios().get(idEnvio);
    if (e != null) {
      e.setEstado(ShipmentStatus.INCIDENCIA);
    }
  }

  /** {@inheritDoc} */
  @Override
  public String metricasResumen(String desde, String hasta) {
    long total = store.envios().size();
    long entregados =
        store.envios().values().stream()
            .filter(e -> e.getEstado() == ShipmentStatus.ENTREGADO)
            .count();
    long incidencias =
        store.envios().values().stream()
            .filter(e -> e.getEstado() == ShipmentStatus.INCIDENCIA)
            .count();
    return "Total:" + total + ", Entregados:" + entregados + ", Incidencias:" + incidencias;
  }

  private EnvioDTO mapEnvio(Envio e) {
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
        e.getServicios().stream().map(Enum::name).collect(Collectors.toSet()));
  }
}
