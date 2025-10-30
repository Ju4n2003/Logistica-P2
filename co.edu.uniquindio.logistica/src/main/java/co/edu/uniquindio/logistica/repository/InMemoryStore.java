package co.edu.uniquindio.logistica.repository;

import co.edu.uniquindio.logistica.model.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AlmacÃ©n en memoria (Singleton) para entidades del mÃ³dulo de logÃ­stica. Provee generaciÃ³n de ids,
 * mapas concurrentes por entidad y credenciales demo.
 */
public class InMemoryStore {
  private static final InMemoryStore INSTANCE = new InMemoryStore();

  /** Obtiene la instancia Ãºnica del almacÃ©n en memoria. */
  public static InMemoryStore getInstance() {
    return INSTANCE;
  }

  private final Map<String, Usuario> usuarios = new ConcurrentHashMap<>();
  private final Map<String, Repartidor> repartidores = new ConcurrentHashMap<>();
  private final Map<String, Direccion> direcciones = new ConcurrentHashMap<>();
  private final Map<String, Envio> envios = new ConcurrentHashMap<>();
  private final Map<String, Pago> pagos = new ConcurrentHashMap<>();

  private final Map<String, String> credentials = new ConcurrentHashMap<>(); // correo -> password

  private InMemoryStore() {
    seedDemoDataOnce();
  }

  /**
   * Genera un id Ãºnico con prefijo.
   *
   * @param prefix prefijo del id (p. ej. USR, ENV)
   * @return id generado
   */
  public String nextId(String prefix) {
    return prefix + "-" + UUID.randomUUID();
  }

  /**
   * @return mapa de usuarios por id
   */
  public Map<String, Usuario> usuarios() {
    return usuarios;
  }

  /**
   * @return mapa de repartidores por id
   */
  public Map<String, Repartidor> repartidores() {
    return repartidores;
  }

  /**
   * @return mapa de direcciones por id
   */
  public Map<String, Direccion> direcciones() {
    return direcciones;
  }

  /**
   * @return mapa de envÃ­os por id
   */
  public Map<String, Envio> envios() {
    return envios;
  }

  /**
   * @return mapa de pagos por id
   */
  public Map<String, Pago> pagos() {
    return pagos;
  }

  /**
   * @return mapa de credenciales correo->password
   */
  public Map<String, String> credentials() {
    return credentials;
  }

  private void seedDemoDataOnce() {
    if (!usuarios.isEmpty() || !repartidores.isEmpty() || !envios.isEmpty()) return;

    // Usuarios demo
    Usuario u1 = new Usuario(nextId("USR"), "Alice Demo", "alice@example.com", "+57 3000000001");
    Usuario u2 = new Usuario(nextId("USR"), "Bob Demo", "bob@example.com", "+57 3000000002");
    usuarios.put(u1.getIdUsuario(), u1);
    usuarios.put(u2.getIdUsuario(), u2);
    credentials.put(u1.getCorreo(), "1234");
    credentials.put(u2.getCorreo(), "1234");
    // Admin demo
    credentials.put("admin@logistica.com", "admin");

    // Direcciones demo
    Direccion d11 = new Direccion(nextId("DIR"), "Casa", "Calle 10 #1-23", "Ciudad A");
    Direccion d12 = new Direccion(nextId("DIR"), "Oficina", "Av 5 #9-81", "Ciudad A");
    Direccion d21 = new Direccion(nextId("DIR"), "Casa", "Cra 45 # 10-55", "Ciudad B");
    Direccion d22 = new Direccion(nextId("DIR"), "Oficina", "Calle 7 # 8-12", "Ciudad B");
    direcciones.put(d11.getIdDireccion(), d11);
    direcciones.put(d12.getIdDireccion(), d12);
    direcciones.put(d21.getIdDireccion(), d21);
    direcciones.put(d22.getIdDireccion(), d22);
    u1.getDirecciones().addAll(Arrays.asList(d11, d12));
    u2.getDirecciones().addAll(Arrays.asList(d21, d22));

    // Repartidores demo
    Repartidor r1 =
        new Repartidor(
            nextId("DRI"),
            "Carlos R.",
            "CC1001",
            "+57 3100000001",
            DriverAvailability.ACTIVO,
            "Zona Norte");
    Repartidor r2 =
        new Repartidor(
            nextId("DRI"),
            "Diana R.",
            "CC1002",
            "+57 3100000002",
            DriverAvailability.INACTIVO,
            "Zona Sur");
    repartidores.put(r1.getIdRepartidor(), r1);
    repartidores.put(r2.getIdRepartidor(), r2);

    // EnvÃ­os demo
    Envio e1 = new Envio();
    e1.setIdEnvio(nextId("ENV"));
    e1.setOrigen(d11);
    e1.setDestino(d12);
    e1.setPesoKg(2.5);
    e1.setVolumenM3(0.02);
    e1.setCosto(12000);
    e1.setEstado(ShipmentStatus.SOLICITADO);
    e1.setFechaCreacion(LocalDateTime.now().minusDays(2));
    e1.setUsuario(u1);
    envios.put(e1.getIdEnvio(), e1);

    Envio e2 = new Envio();
    e2.setIdEnvio(nextId("ENV"));
    e2.setOrigen(d21);
    e2.setDestino(d22);
    e2.setPesoKg(5.0);
    e2.setVolumenM3(0.05);
    e2.setCosto(22000);
    e2.setEstado(ShipmentStatus.ENTREGADO);
    e2.setFechaCreacion(LocalDateTime.now().minusDays(8));
    e2.setFechaEstimadaEntrega(LocalDateTime.now().minusDays(7));
    e2.setUsuario(u2);
    e2.setRepartidor(r1);
    envios.put(e2.getIdEnvio(), e2);

    // Pago demo asociado a e2
    Pago p2 = new Pago();
    p2.setIdPago(nextId("PAG"));
    p2.setMonto(e2.getCosto());
    p2.setFecha(LocalDateTime.now().minusDays(8));
    p2.setMetodo("TARJETA");
    p2.setResultado(PaymentResult.APROBADO);
    pagos.put(p2.getIdPago(), p2);
  }
}
