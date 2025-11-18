package co.edu.uniquindio.logistica.service.impl;

import co.edu.uniquindio.logistica.dto.TarifaDTO;
import co.edu.uniquindio.logistica.service.ITarifaStrategy;

public class SimpleTarifaStrategy implements ITarifaStrategy {
  @Override
  public TarifaDTO calcular(
      String origenId, String destinoId, double pesoKg, double volumenM3, boolean prioridad) {
    double base = 5000; // base fija simulada
    double peso = Math.max(0, pesoKg) * 800;
    double volumen = Math.max(0, volumenM3) * 1500;
    double prioridadVal = prioridad ? 4000 : 0;
    double recargos = 0; // futuro: zona, extras
    double total = base + peso + volumen + prioridadVal + recargos;
    return new TarifaDTO(base, peso, volumen, prioridadVal, recargos, total);
  }
}
