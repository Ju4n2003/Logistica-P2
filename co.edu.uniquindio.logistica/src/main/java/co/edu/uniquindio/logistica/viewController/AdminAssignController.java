package co.edu.uniquindio.logistica.viewController;

import co.edu.uniquindio.logistica.dto.EnvioDTO;
import co.edu.uniquindio.logistica.model.ShipmentStatus;
import co.edu.uniquindio.logistica.repository.InMemoryStore;
import co.edu.uniquindio.logistica.service.IAdminService;
import co.edu.uniquindio.logistica.service.impl.AdminServiceImpl;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AdminAssignController {

  @FXML private ListView<String> listaEnvios;
  @FXML private ListView<String> listaRepartidores;
  @FXML private ComboBox<String> estado;
  @FXML private TextField incidencia;
  @FXML private TextField zona;
  @FXML private TextArea detalle;
  @FXML private Label status;

  private final IAdminService adminService = new AdminServiceImpl();
  private final InMemoryStore store = InMemoryStore.getInstance();

  @FXML
  private void initialize() {
    // Estados
    List<String> estados = new ArrayList<>();
    for (ShipmentStatus s : ShipmentStatus.values()) estados.add(s.name());
    estado.getItems().setAll(estados);
    if (!estados.isEmpty()) estado.getSelectionModel().select(0);
    refreshLists();
    listaEnvios
        .getSelectionModel()
        .selectedItemProperty()
        .addListener((o, a, n) -> showEnvioDetail(n));
    listaRepartidores
        .getSelectionModel()
        .selectedItemProperty()
        .addListener((o, a, n) -> showDriverDetail(n));
  }

  private void refreshLists() {
    // Envios
    listaEnvios.getItems().clear();
    for (var e : store.envios().values()) {
      String line =
          e.getIdEnvio()
              + " | "
              + e.getEstado().name()
              + " | "
              + (e.getUsuario() != null ? e.getUsuario().getNombreCompleto() : "-")
              + " | $"
              + e.getCosto();
      listaEnvios.getItems().add(line);
    }
    // Repartidores
    listaRepartidores.getItems().clear();
    for (var r : store.repartidores().values()) {
      String line =
          r.getIdRepartidor()
              + " | "
              + r.getNombre()
              + " | "
              + r.getDisponibilidad().name()
              + " | "
              + r.getZonaCobertura();
      listaRepartidores.getItems().add(line);
    }
    status.setText(
        "EnvÂ­ios:" + store.envios().size() + ", Repartidores:" + store.repartidores().size());
  }

  private String parseId(String line) {
    return line != null ? line.split(" \\|")[0].trim() : null;
  }

  private void showEnvioDetail(String line) {
    if (line == null) {
      detalle.clear();
      return;
    }
    String id = parseId(line);
    var e = store.envios().get(id);
    if (e == null) {
      detalle.clear();
      return;
    }
    StringBuilder sb = new StringBuilder();
    sb.append("ENVIO\n");
    sb.append("ID: ").append(e.getIdEnvio()).append('\n');
    sb.append("Estado: ").append(e.getEstado());
    if (e.getEstado() == co.edu.uniquindio.logistica.model.ShipmentStatus.ENTREGADO) {
      sb.append(" Ã¢Å“â€");
    }
    sb.append('\n');
    sb.append("Usuario: ")
        .append(e.getUsuario() != null ? e.getUsuario().getNombreCompleto() : "-")
        .append('\n');
    sb.append("Repartidor: ")
        .append(e.getRepartidor() != null ? e.getRepartidor().getNombre() : "(sin asignar)")
        .append('\n');
    if (e.getEstado() == co.edu.uniquindio.logistica.model.ShipmentStatus.ENTREGADO
        && e.getFechaEstimadaEntrega() != null) {
      sb.append("Fecha entrega: ").append(e.getFechaEstimadaEntrega()).append('\n');
    } else if (e.getFechaEstimadaEntrega() != null) {
      sb.append("ETA: ").append(e.getFechaEstimadaEntrega()).append('\n');
    }
    sb.append("Costo: $").append(e.getCosto());
    detalle.setText(sb.toString());
  }

  private void showDriverDetail(String line) {
    // opcional, mantener detalle simple en textarea
  }

  @FXML
  private void onAssign() {
    String eSel = listaEnvios.getSelectionModel().getSelectedItem();
    String rSel = listaRepartidores.getSelectionModel().getSelectedItem();
    if (eSel == null || rSel == null) {
      status.setText("Seleccione envio y repartidor");
      return;
    }
    String idE = parseId(eSel);
    String idR = parseId(rSel);
    EnvioDTO dto = adminService.assignEnvio(idE, idR);
    status.setText(dto != null ? ("Asignado a " + dto.repartidorId()) : "Operacion no valida");
    refreshLists();
    showEnvioDetail(listaEnvios.getSelectionModel().getSelectedItem());
  }

  @FXML
  private void onReassign() {
    String eSel = listaEnvios.getSelectionModel().getSelectedItem();
    String rSel = listaRepartidores.getSelectionModel().getSelectedItem();
    if (eSel == null || rSel == null) {
      status.setText("Seleccione envio y repartidor");
      return;
    }
    String idE = parseId(eSel);
    String idR = parseId(rSel);
    EnvioDTO dto = adminService.reassignEnvio(idE, idR);
    status.setText(dto != null ? ("Reasignado a " + dto.repartidorId()) : "Operacion no valida");
    refreshLists();
    showEnvioDetail(listaEnvios.getSelectionModel().getSelectedItem());
  }

  @FXML
  private void onChangeEstado() {
    String eSel = listaEnvios.getSelectionModel().getSelectedItem();
    String est = estado.getSelectionModel().getSelectedItem();
    if (eSel == null || est == null) {
      status.setText("Seleccione envio y estado");
      return;
    }
    String idE = parseId(eSel);
    EnvioDTO dto = adminService.changeEstado(idE, est);
    status.setText(dto != null ? ("Estado: " + dto.estado()) : "Operacion no valida");
    refreshLists();
    showEnvioDetail(listaEnvios.getSelectionModel().getSelectedItem());
  }

  @FXML
  private void onIncidencia() {
    String eSel = listaEnvios.getSelectionModel().getSelectedItem();
    if (eSel == null) {
      status.setText("Seleccione envio");
      return;
    }
    String idE = parseId(eSel);
    adminService.registrarIncidencia(idE, incidencia.getText(), zona.getText());
    status.setText("Incidencia registrada");
    refreshLists();
    showEnvioDetail(listaEnvios.getSelectionModel().getSelectedItem());
  }

  @FXML
  private void onMarkDelivered() {
    String eSel = listaEnvios.getSelectionModel().getSelectedItem();
    if (eSel == null) {
      status.setText("Seleccione envio");
      return;
    }
    String idE = parseId(eSel);
    Alert confirm =
        new Alert(
            Alert.AlertType.CONFIRMATION,
            "Ã‚Â¿Confirmas marcar el envio como ENTREGADO?",
            ButtonType.OK,
            ButtonType.CANCEL);
    confirm.setHeaderText(null);
    var result = confirm.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      EnvioDTO dto =
          adminService.changeEstado(
              idE, co.edu.uniquindio.logistica.model.ShipmentStatus.ENTREGADO.name());
      status.setText(dto != null ? ("Estado: " + dto.estado()) : "Operacion no valida");
      refreshLists();
      showEnvioDetail(listaEnvios.getSelectionModel().getSelectedItem());
    }
  }
}
