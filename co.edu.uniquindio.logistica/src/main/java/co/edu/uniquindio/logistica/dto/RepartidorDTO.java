package co.edu.uniquindio.logistica.dto;

/** Datos de un repartidor (identificacion, contacto, disponibilidad y zona). */
public record RepartidorDTO(
    String idRepartidor,
    String nombre,
    String documento,
    String telefono,
    String disponibilidad,
    String zonaCobertura) {}
