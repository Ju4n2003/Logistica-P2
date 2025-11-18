package co.edu.uniquindio.logistica.command;

/**
 * Interfaz base del patrón Command para acciones de administrador.
 * Encapsula una solicitud como un objeto, permitiendo parametrizar 
 * clientes con diferentes solicitudes, colas o registros.
 * 
 * Aplicación del patrón Command:
 * - Problema: Necesidad de ejecutar acciones administrativas con 
 *   capacidad de deshacer, registrar y encolar sin acoplamiento
 * - Propósito: Desacoplar el objeto que invoca la operación del 
 *   objeto que sabe cómo realizarla
 * - Solución: Interfaz Command que declara método execute() y 
 *   opcionalmente undo() para encapsular acciones
 */
public interface AdminCommand {
    
    /**
     * Ejecuta la acción del comando
     * 
     * @return resultado de la ejecución
     */
    CommandResult execute();
    
    /**
     * Deshace la acción del comando (opcional)
     * 
     * @return resultado de la deshacer
     */
    default CommandResult undo() {
        return new CommandResult(false, "Undo no implementado para este comando");
    }
    
    /**
     * Obtiene una descripción del comando
     */
    String getDescription();
    
    /**
     * Resultado de ejecutar un comando
     */
    class CommandResult {
        private final boolean success;
        private final String message;
        
        public CommandResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        
        @Override
        public String toString() {
            return String.format("[%s] %s", success ? "OK" : "ERROR", message);
        }
    }
}
