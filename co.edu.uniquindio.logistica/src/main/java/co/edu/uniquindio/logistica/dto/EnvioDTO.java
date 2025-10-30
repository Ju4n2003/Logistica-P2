package co.edu.uniquindio.logistica.dto;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO de Envio con ids de origen/destino y referencias a repartidor/usuario. Incluye peso, volumen,
 * costo, estado y fechas clave.
 */
public record EnvioDTO(
    String idEnvio,
    String origenId,
    String destinoId,
    double pesoKg,
    double volumenM3,
    double costo,
    String estado,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaEstimadaEntrega,
    String repartidorId,
    String usuarioId,
    Set<String> servicios) {}
