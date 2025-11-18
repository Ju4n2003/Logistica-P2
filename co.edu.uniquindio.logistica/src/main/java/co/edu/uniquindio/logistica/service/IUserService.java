package co.edu.uniquindio.logistica.service;

import co.edu.uniquindio.logistica.dto.*;
import java.util.List;

/** Casos de uso del usuario final para la plataforma de logÃ­stica. */
public interface IUserService {
  /** Registra un nuevo usuario. */
  UsuarioDTO register(UsuarioDTO usuario, String password);

  /** Autentica un usuario por correo y contraseÃ±a. */
  UsuarioDTO login(String correo, String password);

  /** Actualiza datos bÃ¡sicos del perfil. */
  UsuarioDTO updateProfile(UsuarioDTO usuario);

  /** Agrega una direcciÃ³n frecuente al usuario. */
  DireccionDTO addDireccion(String idUsuario, DireccionDTO direccion);

  /** Elimina una direcciÃ³n frecuente del usuario. */
  void removeDireccion(String idUsuario, String idDireccion);

  /** Calcula una tarifa estimada para un envÃ­o. */
  TarifaDTO quote(
      String origenId, String destinoId, double pesoKg, double volumenM3, boolean prioridad);

  /** Crea un nuevo envÃ­o a partir de una cotizaciÃ³n. */
  EnvioDTO createEnvio(EnvioDTO envio);

  /** Cancela un envÃ­o si aÃºn estÃ¡ en estado SOLICITADO. */
  void cancelEnvio(String idEnvio);

  /** Simula el pago de un envÃ­o. */
  PagoDTO pay(String idEnvio, String metodo);

  /** Consulta el detalle del envÃ­o. */
  EnvioDTO track(String idEnvio);

  /** Devuelve el historial filtrado del usuario. */
  List<EnvioDTO> historial(String idUsuario, String estado, String desde, String hasta);
}
