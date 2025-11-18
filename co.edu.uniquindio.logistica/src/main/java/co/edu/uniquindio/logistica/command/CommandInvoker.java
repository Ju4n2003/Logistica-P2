package co.edu.uniquindio.logistica.command;

import java.util.ArrayList;
import java.util.List;

/**
 * Invocador del patrón Command que gestiona la ejecución de comandos.
 * Mantiene un historial de comandos ejecutados y permite deshacer acciones.
 * 
 * Aplicación del patrón Command:
 * - Problema: Necesidad de gestionar múltiples comandos con capacidad 
 *   de deshacer y registro de acciones
 * - Propósito: Proporcionar un punto centralizado para ejecutar, 
 *   deshacer y mantener historial de comandos
 * - Solución: Clase invocadora que mantiene listas de comandos 
 *   y gestiona su ejecución y deshacer
 */
public class CommandInvoker {
    
    private final List<AdminCommand> commandHistory = new ArrayList<>();
    private final List<AdminCommand> undoHistory = new ArrayList<>();
    
    /**
     * Ejecuta un comando y lo agrega al historial
     */
    public AdminCommand.CommandResult executeCommand(AdminCommand command) {
        AdminCommand.CommandResult result = command.execute();
        
        if (result.isSuccess()) {
            commandHistory.add(command);
            undoHistory.clear(); // Limpiar historial de undo cuando se ejecuta un nuevo comando
        }
        
        return result;
    }
    
    /**
     * Deshace el último comando ejecutado
     */
    public AdminCommand.CommandResult undoLastCommand() {
        if (commandHistory.isEmpty()) {
            return new AdminCommand.CommandResult(false, "No hay comandos para deshacer");
        }
        
        AdminCommand lastCommand = commandHistory.remove(commandHistory.size() - 1);
        AdminCommand.CommandResult result = lastCommand.undo();
        
        if (result.isSuccess()) {
            undoHistory.add(lastCommand);
        } else {
            // Si el undo falla, devolver el comando al historial
            commandHistory.add(lastCommand);
        }
        
        return result;
    }
    
    /**
     * Rehace el último comando deshecho
     */
    public AdminCommand.CommandResult redoLastCommand() {
        if (undoHistory.isEmpty()) {
            return new AdminCommand.CommandResult(false, "No hay comandos para rehacer");
        }
        
        AdminCommand commandToRedo = undoHistory.remove(undoHistory.size() - 1);
        AdminCommand.CommandResult result = commandToRedo.execute();
        
        if (result.isSuccess()) {
            commandHistory.add(commandToRedo);
        }
        
        return result;
    }
    
    /**
     * Obtiene el historial de comandos ejecutados
     */
    public List<String> getCommandHistory() {
        List<String> history = new ArrayList<>();
        for (AdminCommand command : commandHistory) {
            history.add(command.getDescription());
        }
        return history;
    }
    
    /**
     * Limpia todo el historial
     */
    public void clearHistory() {
        commandHistory.clear();
        undoHistory.clear();
    }
    
    /**
     * Obtiene estadísticas de comandos
     */
    public CommandStats getStats() {
        return new CommandStats(commandHistory.size(), undoHistory.size());
    }
    
    /**
     * Estadísticas de comandos
     */
    public static class CommandStats {
        private final int executedCount;
        private final int undoneCount;
        
        public CommandStats(int executedCount, int undoneCount) {
            this.executedCount = executedCount;
            this.undoneCount = undoneCount;
        }
        
        public int getExecutedCount() { return executedCount; }
        public int getUndoneCount() { return undoneCount; }
        
        @Override
        public String toString() {
            return String.format("Ejecutados: %d, Deshechos: %d", executedCount, undoneCount);
        }
    }
}
