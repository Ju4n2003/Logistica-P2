package co.edu.uniquindio.logistica.viewController;

import co.edu.uniquindio.logistica.model.Pago;
import co.edu.uniquindio.logistica.model.PaymentResult;
import co.edu.uniquindio.logistica.model.ShipmentStatus;
import co.edu.uniquindio.logistica.repository.InMemoryStore;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class AdminMetricsController {

  @FXML private DatePicker desde;
  @FXML private DatePicker hasta;
  @FXML private PieChart chartEstados;
  @FXML private BarChart<String, Number> chartIngresos;
  @FXML private LineChart<String, Number> chartEnviosDia;
  @FXML private Label status;

  private final InMemoryStore store = InMemoryStore.getInstance();
  private final DateTimeFormatter DAY_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private final DateTimeFormatter MONTH_FMT = DateTimeFormatter.ofPattern("yyyy-MM");

  @FXML
  private void initialize() {
    // Rango por defecto: ultimos 30 dias
    LocalDate today = LocalDate.now();
    desde.setValue(today.minusDays(30));
    hasta.setValue(today);
    apply();
  }

  @FXML
  private void onApply() {
    apply();
  }

  private void apply() {
    LocalDate d = desde.getValue();
    LocalDate h = hasta.getValue();
    if (d != null && h != null && d.isAfter(h)) {
      // Corrige si el rango viene invertido
      LocalDate tmp = d;
      d = h;
      h = tmp;
    }
    renderEstados(d, h);
    renderIngresos(d, h);
    renderEnviosDia(d, h);
    status.setText("Rango aplicado: " + (d != null ? d : "-") + " a " + (h != null ? h : "-"));
  }

  private boolean inRange(LocalDate date, LocalDate d, LocalDate h) {
    if (date == null) return false;
    if (d != null && date.isBefore(d)) return false;
    if (h != null && date.isAfter(h)) return false;
    return true;
  }

  private void renderEstados(LocalDate d, LocalDate h) {
    chartEstados.getData().clear();
    Map<ShipmentStatus, Integer> counts = new HashMap<>();
    for (var e : store.envios().values()) {
      LocalDate day = e.getFechaCreacion() != null ? e.getFechaCreacion().toLocalDate() : null;
      if (!inRange(day, d, h)) continue;
      counts.merge(e.getEstado(), 1, Integer::sum);
    }
    for (var entry : counts.entrySet()) {
      chartEstados.getData().add(new PieChart.Data(entry.getKey().name(), entry.getValue()));
    }
  }

  private void renderIngresos(LocalDate d, LocalDate h) {
    chartIngresos.getData().clear();
    Map<String, Double> byMonth = new HashMap<>();
    for (Pago p : store.pagos().values()) {
      LocalDate day = p.getFecha() != null ? p.getFecha().toLocalDate() : null;
      if (!inRange(day, d, h)) continue;
      if (p.getResultado() != PaymentResult.APROBADO) continue;
      String month = day.format(MONTH_FMT);
      byMonth.merge(month, p.getMonto(), Double::sum);
    }
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    byMonth.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(
            e -> {
              series.getData().add(new XYChart.Data<>(e.getKey(), e.getValue()));
            });
    chartIngresos.getData().add(series);
  }

  private void renderEnviosDia(LocalDate d, LocalDate h) {
    chartEnviosDia.getData().clear();
    Map<String, Integer> byDay = new HashMap<>();
    for (var e : store.envios().values()) {
      LocalDate day = e.getFechaCreacion() != null ? e.getFechaCreacion().toLocalDate() : null;
      if (!inRange(day, d, h)) continue;
      String key = day.format(DAY_FMT);
      byDay.merge(key, 1, Integer::sum);
    }
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    byDay.entrySet().stream()
        .sorted(Map.Entry.comparingByKey())
        .forEach(
            e -> {
              series.getData().add(new XYChart.Data<>(e.getKey(), e.getValue()));
            });
    chartEnviosDia.getData().add(series);
  }

  @FXML
  private void onExportCsv() {
    LocalDate d = desde.getValue();
    LocalDate h = hasta.getValue();
    try {
      Path dir = Paths.get("exports");
      Files.createDirectories(dir);
      String name =
          String.format(
              "metrics_%s.csv",
              LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")));
      Path out = dir.resolve(name);

      // Agregaciones simples
      Map<String, Integer> estados = new HashMap<>();
      Map<String, Double> ingresosMes = new HashMap<>();
      Map<String, Integer> enviosDia = new HashMap<>();

      for (var e : store.envios().values()) {
        LocalDate day = e.getFechaCreacion() != null ? e.getFechaCreacion().toLocalDate() : null;
        if (!inRange(day, d, h)) continue;
        estados.merge(e.getEstado().name(), 1, Integer::sum);
        enviosDia.merge(day.format(DAY_FMT), 1, Integer::sum);
      }
      for (Pago p : store.pagos().values()) {
        LocalDate day = p.getFecha() != null ? p.getFecha().toLocalDate() : null;
        if (!inRange(day, d, h)) continue;
        if (p.getResultado() != PaymentResult.APROBADO) continue;
        ingresosMes.merge(day.format(MONTH_FMT), p.getMonto(), Double::sum);
      }

      StringBuilder sb = new StringBuilder();
      sb.append("Rango,")
          .append(d != null ? d : "")
          .append(",")
          .append(h != null ? h : "")
          .append("\n");
      sb.append("Estados,Conteo\n");
      estados.entrySet().stream()
          .sorted(Map.Entry.comparingByKey())
          .forEach(e -> sb.append(e.getKey()).append(",").append(e.getValue()).append("\n"));
      sb.append("\nIngresos por Mes,Valor\n");
      ingresosMes.entrySet().stream()
          .sorted(Map.Entry.comparingByKey())
          .forEach(e -> sb.append(e.getKey()).append(",").append(e.getValue()).append("\n"));
      sb.append("\nEnvios por Dia,Conteo\n");
      enviosDia.entrySet().stream()
          .sorted(Map.Entry.comparingByKey())
          .forEach(e -> sb.append(e.getKey()).append(",").append(e.getValue()).append("\n"));

      Files.writeString(out, sb.toString());
      status.setText("CSV exportado: " + out.toAbsolutePath());
    } catch (IOException ex) {
      status.setText("Error exportando CSV: " + ex.getMessage());
    }
  }

  @FXML
  private void onExportPdf() {
    LocalDate d = desde.getValue();
    LocalDate h = hasta.getValue();
    try (PDDocument doc = new PDDocument()) {
      PDPage page = new PDPage(PDRectangle.LETTER);
      doc.addPage(page);
      try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
        float y = page.getMediaBox().getHeight() - 50;
        cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
        cs.beginText();
        cs.newLineAtOffset(50, y);
        cs.showText("Reporte de MÃ©tricas - LogÃ­stica");
        cs.endText();
        y -= 20;
        cs.setFont(PDType1Font.HELVETICA, 11);
        String rango = "Rango: " + (d != null ? d : "") + " a " + (h != null ? h : "");
        cs.beginText();
        cs.newLineAtOffset(50, y);
        cs.showText(rango);
        cs.endText();
        y -= 20;

        // Estados
        Map<String, Integer> estados = new HashMap<>();
        for (var e : store.envios().values()) {
          LocalDate day = e.getFechaCreacion() != null ? e.getFechaCreacion().toLocalDate() : null;
          if (!inRange(day, d, h)) continue;
          estados.merge(e.getEstado().name(), 1, Integer::sum);
        }
        cs.beginText();
        cs.newLineAtOffset(50, y);
        cs.showText("Estados:");
        cs.endText();
        y -= 16;
        for (var entry : estados.entrySet()) {
          cs.beginText();
          cs.newLineAtOffset(60, y);
          cs.showText(entry.getKey() + ": " + entry.getValue());
          cs.endText();
          y -= 14;
          if (y < 80) {
            break;
          }
        }

        // Ingresos por mes
        Map<String, Double> ingresosMes = new HashMap<>();
        for (Pago p : store.pagos().values()) {
          LocalDate day = p.getFecha() != null ? p.getFecha().toLocalDate() : null;
          if (!inRange(day, d, h)) continue;
          if (p.getResultado() != PaymentResult.APROBADO) continue;
          ingresosMes.merge(day.format(MONTH_FMT), p.getMonto(), Double::sum);
        }
        cs.beginText();
        cs.newLineAtOffset(50, y);
        cs.showText("Ingresos por Mes:");
        cs.endText();
        y -= 16;
        for (var entry : ingresosMes.entrySet()) {
          cs.beginText();
          cs.newLineAtOffset(60, y);
          cs.showText(entry.getKey() + ": $" + entry.getValue());
          cs.endText();
          y -= 14;
          if (y < 80) {
            break;
          }
        }

        // Envios por dia
        Map<String, Integer> enviosDia = new HashMap<>();
        for (var e : store.envios().values()) {
          LocalDate day = e.getFechaCreacion() != null ? e.getFechaCreacion().toLocalDate() : null;
          if (!inRange(day, d, h)) continue;
          enviosDia.merge(day.format(DAY_FMT), 1, Integer::sum);
        }
        cs.beginText();
        cs.newLineAtOffset(50, y);
        cs.showText("EnvÃ­os por DÃ­a:");
        cs.endText();
        y -= 16;
        for (var entry : enviosDia.entrySet()) {
          cs.beginText();
          cs.newLineAtOffset(60, y);
          cs.showText(entry.getKey() + ": " + entry.getValue());
          cs.endText();
          y -= 14;
          if (y < 80) {
            break;
          }
        }
      }
      Path dir = Paths.get("exports");
      Files.createDirectories(dir);
      Path out =
          dir.resolve(
              "metrics_"
                  + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                  + ".pdf");
      doc.save(out.toFile());
      status.setText("PDF exportado: " + out.toAbsolutePath());
    } catch (IOException ex) {
      status.setText("Error exportando PDF: " + ex.getMessage());
    }
  }
}
