package co.edu.uniquindio.logistica.builder;

import co.edu.uniquindio.logistica.model.Usuario;
import co.edu.uniquindio.logistica.model.Direccion;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder para la construcción de objetos Usuario de forma flexible.
 * Implementa el patrón creacional Builder para permitir la construcción
 * paso a paso de usuarios con configuraciones complejas.
 */
public class UsuarioBuilder {
    private String idUsuario;
    private String nombreCompleto;
    private String correo;
    private String telefono;
    private List<Direccion> direcciones = new ArrayList<>();
    
    /**
     * Establece el ID del usuario
     */
    public UsuarioBuilder withId(String idUsuario) {
        this.idUsuario = idUsuario;
        return this;
    }
    
    /**
     * Establece el nombre completo del usuario
     */
    public UsuarioBuilder withNombre(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
        return this;
    }
    
    /**
     * Establece el correo electrónico del usuario
     */
    public UsuarioBuilder withCorreo(String correo) {
        this.correo = correo;
        return this;
    }
    
    /**
     * Establece el teléfono del usuario
     */
    public UsuarioBuilder withTelefono(String telefono) {
        this.telefono = telefono;
        return this;
    }
    
    /**
     * Agrega una dirección a la lista de direcciones del usuario
     */
    public UsuarioBuilder addDireccion(Direccion direccion) {
        this.direcciones.add(direccion);
        return this;
    }
    
    /**
     * Establece la lista completa de direcciones
     */
    public UsuarioBuilder withDirecciones(List<Direccion> direcciones) {
        this.direcciones = new ArrayList<>(direcciones);
        return this;
    }
    
    /**
     * Construye el objeto Usuario con las configuraciones establecidas
     * @throws IllegalStateException si faltan datos obligatorios
     */
    public Usuario build() {
        validarDatosObligatorios();
        
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(this.idUsuario);
        usuario.setNombreCompleto(this.nombreCompleto);
        usuario.setCorreo(this.correo);
        usuario.setTelefono(this.telefono);
        usuario.setDirecciones(new ArrayList<>(this.direcciones));
        
        return usuario;
    }
    
    /**
     * Valida que los datos obligatorios estén presentes
     */
    private void validarDatosObligatorios() {
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new IllegalStateException("El nombre completo es obligatorio");
        }
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalStateException("El correo es obligatorio");
        }
        if (!correo.contains("@")) {
            throw new IllegalStateException("El correo debe ser válido");
        }
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalStateException("El teléfono es obligatorio");
        }
    }
    
    /**
     * Método estático de conveniencia para iniciar la construcción
     */
    public static UsuarioBuilder builder() {
        return new UsuarioBuilder();
    }
}
