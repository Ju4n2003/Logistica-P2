package co.edu.uniquindio.logistica.dto;

/** Desglose de una cotizacion de tarifa (base, peso, volumen, prioridad, recargos y total). */
public record TarifaDTO(
    double base, double peso, double volumen, double prioridad, double recargos, double total) {}
