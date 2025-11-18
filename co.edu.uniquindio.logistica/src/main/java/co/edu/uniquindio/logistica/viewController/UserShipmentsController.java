package co.edu.uniquindio.logistica.viewController;

import co.edu.uniquindio.logistica.Session;
import co.edu.uniquindio.logistica.dto.EnvioDTO;
import co.edu.uniquindio.logistica.dto.PagoDTO;
import co.edu.uniquindio.logistica.dto.UsuarioDTO;
import co.edu.uniquindio.logistica.service.IUserService;
import co.edu.uniquindio.logistica.service.impl.UserServiceImpl;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class UserShipmentsController {

  @FXML private ListView<String> listaEnvios;
  @FXML private TextArea detalle;
  @FXML private Label status;
  @FXML private DatePicker desde;
  @FXML private DatePicker hasta;
  @FXML private ComboBox<String> estado;

  private final IUserService userService = new UserServiceImpl();

  @FXML
  private void initialize() {
    // Inicializar rango por defecto y estados
    LocalDate today = LocalDate.now();
    if (desde != null) desde.setValue(today.minusDays(30));
    if (hasta != null) hasta.setValue(today);
    if (estado != null) {
      estado.getItems().setAll("", "SOLICITADO", "ASIGNADO", "EN_RUTA", "ENTREGADO", "INCIDENCIA");
      estado.getSelectionModel().select(0);
    }
    onApply();
    listaEnvios
        .getSelectionModel()
        .selectedItemProperty()
        .addListener((obs, o, n) -> showDetail(n));
  }

  @FXML
  private void onRefresh() {
    listaEnvios.getItems().clear();
    UsuarioDTO u = Session.getCurrentUser();
    if (u == null) {
      status.setText("No autenticado");
      return;
    }
    String est = estado != null ? estado.getSelectionModel().getSelectedItem() : null;
    if (est != null && est.isBlank()) est = null;
    String d = (desde != null && desde.getValue() != null) ? desde.getValue().toString() : null;
    String h = (hasta != null && hasta.getValue() != null) ? hasta.getValue().toString() : null;
    List<EnvioDTO> envios = userService.historial(u.idUsuario(), est, d, h);
    for (EnvioDTO e : envios) {
      String line = e.idEnvio() + " | Estado:" + e.estado() + " | Costo:" + e.costo();
      listaEnvios.getItems().add(line);
    }
    status.setText("Envios: " + envios.size());
  }

  @FXML
  private void onTrack() {
    String sel = listaEnvios.getSelectionModel().getSelectedItem();
    if (sel == null) return;
    String id = sel.split(" \\|")[0].trim();
    EnvioDTO e = userService.track(id);
    if (e == null) return;
    detalle.setText(
        "ID: "
            + e.idEnvio()
            + "\n"
            + "Estado: "
            + e.estado()
            + "\n"
            + "Costo: "
            + e.costo()
            + "\n"
            + "Fecha: "
            + e.fechaCreacion()
            + "\n"
            + "Repartidor: "
            + (e.repartidorId() != null ? e.repartidorId() : "(sin asignar)"));
  }

  @FXML
  private void onPay() {
    String sel = listaEnvios.getSelectionModel().getSelectedItem();
    if (sel == null) return;
    String id = sel.split(" \\|")[0].trim();
    PagoDTO pago = userService.pay(id, "TARJETA");
    if (pago != null) {
      status.setText("Pago " + pago.resultado() + " - " + pago.idPago());
    }
    onTrack();
  }

  private void showDetail(String line) {
    if (line == null) {
      detalle.clear();
      return;
    }
    String id = line.split(" \\|")[0].trim();
    EnvioDTO e = userService.track(id);
    if (e == null) {
      detalle.clear();
      return;
    }
    detalle.setText(
        "ID: "
            + e.idEnvio()
            + "\n"
            + "Estado: "
            + e.estado()
            + "\n"
            + "Costo: "
            + e.costo()
            + "\n"
            + "Fecha: "
            + e.fechaCreacion());
  }

  @FXML
  private void onApply() {
    onRefresh();
  }

  @FXML
  private void onExportCsv() {
    try {
      UsuarioDTO u = Session.getCurrentUser();
      if (u == null) {
        status.setText("No autenticado");
        return;
      }
      String est = estado != null ? estado.getSelectionModel().getSelectedItem() : null;
      if (est != null && est.isBlank()) est = null;
      String d = (desde != null && desde.getValue() != null) ? desde.getValue().toString() : null;
      String h = (hasta != null && hasta.getValue() != null) ? hasta.getValue().toString() : null;
      List<EnvioDTO> envios = userService.historial(u.idUsuario(), est, d, h);

      Path dir = Paths.get("exports");
      Files.createDirectories(dir);
      Path out =
          dir.resolve(
              "mis_envios_"
                  + java.time.LocalDateTime.now()
                      .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                  + ".csv");
      StringBuilder sb = new StringBuilder();
      sb.append("ID,Estado,Costo,Fecha,Repartidor\n");
      for (EnvioDTO e : envios) {
        sb.append(e.idEnvio())
            .append(",")
            .append(e.estado())
            .append(",")
            .append(e.costo())
            .append(",")
            .append(e.fechaCreacion())
            .append(",")
            .append(e.repartidorId() != null ? e.repartidorId() : "")
            .append("\n");
      }
      Files.writeString(out, sb.toString());
      status.setText("CSV exportado: " + out.toAbsolutePath());
    } catch (IOException ex) {
      status.setText("Error exportando CSV: " + ex.getMessage());
    }
  }

  @FXML
  private void onExportPdf() {
    try {
      UsuarioDTO u = Session.getCurrentUser();
      if (u == null) {
        status.setText("No autenticado");
        return;
      }
      String est = estado != null ? estado.getSelectionModel().getSelectedItem() : null;
      if (est != null && est.isBlank()) est = null;
      String d = (desde != null && desde.getValue() != null) ? desde.getValue().toString() : null;
      String h = (hasta != null && hasta.getValue() != null) ? hasta.getValue().toString() : null;
      List<EnvioDTO> envios = userService.historial(u.idUsuario(), est, d, h);

      Path dir = Paths.get("exports");
      Files.createDirectories(dir);
      Path out =
          dir.resolve(
              "mis_envios_"
                  + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                  + ".pdf");

      try (PDDocument doc = new PDDocument()) {
        PDPage page = new PDPage(PDRectangle.LETTER);
        doc.addPage(page);
        try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
          float y = page.getMediaBox().getHeight() - 50;
          cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
          cs.beginText();
          cs.newLineAtOffset(50, y);
          cs.showText("Mis Envios - " + u.nombreCompleto());
          cs.endText();
          y -= 18;
          cs.setFont(PDType1Font.HELVETICA, 11);
          String rango =
              "Rango: "
                  + (d != null ? d : "")
                  + " a "
                  + (h != null ? h : "")
                  + (est != null ? (" | Estado: " + est) : "");
          cs.beginText();
          cs.newLineAtOffset(50, y);
          cs.showText(rango);
          cs.endText();
          y -= 22;

          // Encabezados de tabla
          cs.setFont(PDType1Font.HELVETICA_BOLD, 11);
          writeRow(cs, 50, y, "ID", "Estado", "Costo", "Fecha", "Repartidor");
          y -= 14;
          cs.setFont(PDType1Font.HELVETICA, 11);

          for (EnvioDTO e : envios) {
            String fecha = e.fechaCreacion() != null ? e.fechaCreacion().toString() : "";
            String rep = e.repartidorId() != null ? e.repartidorId() : "";
            writeRow(
                cs,
                50,
                y,
                safe(e.idEnvio()),
                safe(e.estado()),
                String.valueOf(e.costo()),
                fecha,
                rep);
            y -= 14;
            if (y < 80) break; // simple corte si se llena la pagina
          }

          y -= 16;
          cs.setFont(PDType1Font.HELVETICA_BOLD, 11);
          cs.beginText();
          cs.newLineAtOffset(50, y);
          cs.showText("Total envios: " + envios.size());
          cs.endText();
        }
        doc.save(out.toFile());
      }
      status.setText("PDF exportado: " + out.toAbsolutePath());
    } catch (IOException ex) {
      status.setText("Error exportando PDF: " + ex.getMessage());
    }
  }

  private void writeRow(
      PDPageContentStream cs,
      float x,
      float y,
      String c1,
      String c2,
      String c3,
      String c4,
      String c5)
      throws IOException {
    float[] widths = new float[] {160, 80, 70, 140, 100};
    String[] cols = new String[] {c1, c2, c3, c4, c5};
    float cx = x;
    for (int i = 0; i < cols.length; i++) {
      cs.beginText();
      cs.newLineAtOffset(cx, y);
      cs.showText(truncate(cols[i], 30));
      cs.endText();
      cx += widths[i];
    }
  }

  private String truncate(String s, int max) {
    if (s == null) return "";
    return s.length() <= max ? s : s.substring(0, max - 1) + "â€¦";
  }     

  private String safe(String s) {
    return s == null ? "" : s;
  }
}
