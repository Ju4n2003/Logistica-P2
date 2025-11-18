package co.edu.uniquindio.logistica.command;

import co.edu.uniquindio.logistica.model.Usuario;
import co.edu.uniquindio.logistica.repository.InMemoryStore;
import java.util.UUID;

/**
 * Comando concreto para crear un nuevo usuario en el sistema.
 * Implementa el patrón Command para encapsular la lógica de creación.
 * 
 * Aplicación del patrón Command:
 * - Problema: Necesidad de encapsular la creación de usuarios como 
 *   una acción reversible y registrable
 * - Propósito: Permitir que la creación de usuarios sea tratada 
 *   como un comando con capacidad de deshacer
 * - Solución: Clase concreta que implementa AdminCommand y 
 *   encapsula toda la lógica de creación de usuarios
 */
public class CreateUserCommand implements AdminCommand {
    
    private final InMemoryStore store;
    private final String nombre;
    private final String email;
    private final String telefono;
    private final String tipoUsuario;
    
    private String createdUserId; // Guardar referencia para undo
    
    public CreateUserCommand(InMemoryStore store, String nombre, String email, String telefono, String tipoUsuario) {
        this.store = store;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.tipoUsuario = tipoUsuario;
    }
    
    @Override
    public CommandResult execute() {
        try {
            // Verificar si ya existe un usuario con ese email
            boolean exists = store.usuarios().values().stream()
                .anyMatch(u -> u.getCorreo().equals(email));
            
            if (exists) {
                return new CommandResult(false, "Ya existe un usuario con email: " + email);
            }
            
            // Crear nuevo usuario
            Usuario usuario = new Usuario();
            usuario.setIdUsuario("user-" + UUID.randomUUID().toString().substring(0, 8));
            usuario.setNombreCompleto(nombre);
            usuario.setCorreo(email);
            usuario.setTelefono(telefono);
            
            // Guardar usuario
            store.usuarios().put(usuario.getIdUsuario(), usuario);
            createdUserId = usuario.getIdUsuario();
            
            return new CommandResult(true, "Usuario creado exitosamente con ID: " + createdUserId);
            
        } catch (Exception e) {
            return new CommandResult(false, "Error al crear usuario: " + e.getMessage());
        }
    }
    
    @Override
    public CommandResult undo() {
        if (createdUserId == null) {
            return new CommandResult(false, "No se puede deshacer: el comando no fue ejecutado");
        }
        
        try {
            Usuario removed = store.usuarios().remove(createdUserId);
            if (removed != null) {
                return new CommandResult(true, "Usuario eliminado: " + removed.getNombreCompleto());
            } else {
                return new CommandResult(false, "Usuario no encontrado para deshacer");
            }
        } catch (Exception e) {
            return new CommandResult(false, "Error al deshacer creación: " + e.getMessage());
        }
    }
    
    @Override
    public String getDescription() {
        return String.format("Crear usuario: %s (%s)", nombre, email);
    }
}
