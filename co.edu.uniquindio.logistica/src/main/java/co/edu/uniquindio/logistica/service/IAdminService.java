package co.edu.uniquindio.logistica.service;

import co.edu.uniquindio.logistica.dto.*;
import java.util.List;

/** Casos de uso para administraciÃ³n y operaciones. */
public interface IAdminService {
  /** Crea un usuario. */
  UsuarioDTO createUsuario(UsuarioDTO usuario);

  /** Actualiza un usuario existente. */
  UsuarioDTO updateUsuario(UsuarioDTO usuario);

  /** Elimina un usuario por id. */
  void deleteUsuario(String idUsuario);

  /** Lista todos los usuarios. */
  List<UsuarioDTO> listUsuarios();

  /** Crea un repartidor. */
  RepartidorDTO createRepartidor(RepartidorDTO repartidor);

  /** Actualiza un repartidor. */
  RepartidorDTO updateRepartidor(RepartidorDTO repartidor);

  /** Elimina un repartidor por id. */
  void deleteRepartidor(String idRepartidor);

  /** Lista los repartidores. */
  List<RepartidorDTO> listRepartidores();

  /** Asigna un envÃ­o a un repartidor. */
  EnvioDTO assignEnvio(String idEnvio, String idRepartidor);

  /** Reasigna un envÃ­o a otro repartidor. */
  EnvioDTO reassignEnvio(String idEnvio, String idRepartidor);

  /** Cambia el estado del envÃ­o. */
  EnvioDTO changeEstado(String idEnvio, String estado);

  /** Registra una incidencia operativa para un envÃ­o. */
  void registrarIncidencia(String idEnvio, String descripcion, String zona);

  /** Devuelve un resumen textual de mÃ©tricas del periodo. */
  String metricasResumen(String desde, String hasta);
}
