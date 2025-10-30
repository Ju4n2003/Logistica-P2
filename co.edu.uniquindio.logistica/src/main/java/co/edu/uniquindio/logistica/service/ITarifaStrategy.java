package co.edu.uniquindio.logistica.service;

import co.edu.uniquindio.logistica.dto.TarifaDTO;

/** Estrategia para cÃ¡lculo de tarifas de envÃ­o (Strategy Pattern). */
public interface ITarifaStrategy {
  /**
   * Calcula la tarifa estimada dado origen/destino, peso y volumen.
   *
   * @param origenId id de direcciÃ³n origen
   * @param destinoId id de direcciÃ³n destino
   * @param pesoKg peso en kilogramos
   * @param volumenM3 volumen en metros cÃºbicos
   * @param prioridad si aplica prioridad/urgencia
   * @return desglose y total de tarifa
   */
  TarifaDTO calcular(
      String origenId, String destinoId, double pesoKg, double volumenM3, boolean prioridad);
}
