package co.edu.uniquindio.logistica;

import co.edu.uniquindio.logistica.dto.UsuarioDTO;

/**
 * Gestor de sesion simple basado en estado estatico. Mantiene el usuario autenticado actual para la
 * aplicacion de escritorio.
 */
public final class Session {
  private static UsuarioDTO currentUser;
  private static boolean admin;

  private Session() {}

  /**
   * Obtiene el usuario autenticado actual.
   *
   * @return usuario actual o {@code null} si no hay sesion
   */
  public static UsuarioDTO getCurrentUser() {
    return currentUser;
  }

  /**
   * Establece el usuario autenticado actual.
   *
   * @param user usuario autenticado
   */
  public static void setCurrentUser(UsuarioDTO user) {
    currentUser = user;
  }

  /**
   * Indica si existe una sesion activa.
   *
   * @return {@code true} si hay usuario autenticado
   */
  public static boolean isLoggedIn() {
    return currentUser != null;
  }

  /** Inicia sesion como administrador (para panel de administracion). */
  public static void loginAsAdmin() {
    admin = true;
  }

  /**
   * Indica si la sesion actual es de administrador.
   *
   * @return {@code true} si hay sesion de administrador
   */
  public static boolean isAdmin() {
    return admin;
  }

  /** Cierra la sesion actual. */
  public static void logout() {
    currentUser = null;
    admin = false;
  }
}
