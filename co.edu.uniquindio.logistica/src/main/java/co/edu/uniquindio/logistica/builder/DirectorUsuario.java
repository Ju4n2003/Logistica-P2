package co.edu.uniquindio.logistica.builder;

import co.edu.uniquindio.logistica.model.Usuario;
import co.edu.uniquindio.logistica.model.Direccion;
import co.edu.uniquindio.logistica.repository.InMemoryStore;

/**
 * Director que coordina la construcción de usuarios con configuraciones predefinidas.
 * Implementa el patrón Builder en conjunto con UsuarioBuilder para crear
 * usuarios con configuraciones típicas de la plataforma.
 */
public class DirectorUsuario {
    
    private final UsuarioBuilder builder;
    private final InMemoryStore store;
    
    public DirectorUsuario() {
        this.builder = UsuarioBuilder.builder();
        this.store = InMemoryStore.getInstance();
    }
    
    /**
     * Construye un usuario básico con datos mínimos
     */
    public Usuario construirUsuarioBasico(String nombre, String correo, String telefono) {
        return builder
            .withId(store.nextId("USR"))
            .withNombre(nombre)
            .withCorreo(correo)
            .withTelefono(telefono)
            .build();
    }
    
    /**
     * Construye un usuario con dirección principal
     */
    public Usuario construirUsuarioConDireccion(String nombre, String correo, String telefono, Direccion direccion) {
        return builder
            .withId(store.nextId("USR"))
            .withNombre(nombre)
            .withCorreo(correo)
            .withTelefono(telefono)
            .addDireccion(direccion)
            .build();
    }
    
    /**
     * Construye un usuario corporativo con múltiples direcciones
     */
    public Usuario construirUsuarioCorporativo(String nombre, String correo, String telefono, Direccion oficina, Direccion casa) {
        return builder
            .withId(store.nextId("USR"))
            .withNombre(nombre)
            .withCorreo(correo)
            .withTelefono(telefono)
            .addDireccion(oficina)
            .addDireccion(casa)
            .build();
    }
}
