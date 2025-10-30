package co.edu.uniquindio.logistica.viewController;

import co.edu.uniquindio.logistica.dto.RepartidorDTO;
import co.edu.uniquindio.logistica.service.IAdminService;
import co.edu.uniquindio.logistica.service.impl.AdminServiceImpl;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AdminDriversController {

  @FXML private TextField nombre;
  @FXML private TextField documento;
  @FXML private TextField telefono;
  @FXML private ComboBox<String> disponibilidad;
  @FXML private TextField zona;
  @FXML private ListView<String> lista;
  @FXML private Label status;

  private final IAdminService adminService = new AdminServiceImpl();

  @FXML
  private void initialize() {
    disponibilidad.getItems().setAll("ACTIVO", "INACTIVO", "EN_RUTA");
    if (!disponibilidad.getItems().isEmpty()) disponibilidad.getSelectionModel().select(0);
    onRefresh();
    lista
        .getSelectionModel()
        .selectedItemProperty()
        .addListener((obs, o, n) -> loadFromSelection(n));
  }

  @FXML
  private void onCreate() {
    if (isBlank(nombre.getText()) || isBlank(documento.getText())) {
      status.setText("Nombre y documento son obligatorios");
      return;
    }
    RepartidorDTO dto =
        new RepartidorDTO(
            null,
            nombre.getText(),
            documento.getText(),
            telefono.getText(),
            disponibilidad.getSelectionModel().getSelectedItem(),
            zona.getText());
    RepartidorDTO creado = adminService.createRepartidor(dto);
    status.setText(creado != null ? ("Creado: " + creado.idRepartidor()) : "Error al crear");
    clearFields();
    onRefresh();
  }

  @FXML
  private void onUpdate() {
    String sel = lista.getSelectionModel().getSelectedItem();
    if (sel == null) {
      status.setText("Seleccione un repartidor");
      return;
    }
    String id = parseId(sel);
    RepartidorDTO dto =
        new RepartidorDTO(
            id,
            nombre.getText(),
            documento.getText(),
            telefono.getText(),
            disponibilidad.getSelectionModel().getSelectedItem(),
            zona.getText());
    RepartidorDTO upd = adminService.updateRepartidor(dto);
    status.setText(upd != null ? ("Actualizado: " + upd.idRepartidor()) : "No encontrado");
    onRefresh();
  }

  @FXML
  private void onDelete() {
    String sel = lista.getSelectionModel().getSelectedItem();
    if (sel == null) {
      status.setText("Seleccione un repartidor");
      return;
    }
    String id = parseId(sel);
    adminService.deleteRepartidor(id);
    status.setText("Eliminado: " + id);
    clearFields();
    onRefresh();
  }

  @FXML
  private void onRefresh() {
    lista.getItems().clear();
    List<RepartidorDTO> drivers = adminService.listRepartidores();
    for (RepartidorDTO r : drivers) {
      lista
          .getItems()
          .add(
              r.idRepartidor()
                  + " | "
                  + r.nombre()
                  + " | "
                  + r.documento()
                  + " | "
                  + r.telefono()
                  + " | "
                  + r.disponibilidad()
                  + " | "
                  + r.zonaCobertura());
    }
    status.setText("Repartidores: " + drivers.size());
  }

  private void loadFromSelection(String line) {
    if (line == null) return;
    String[] parts = line.split(" \\| ");
    // parts: [id, nombre, documento, telefono, disponibilidad, zona]
    if (parts.length >= 6) {
      nombre.setText(parts[1].trim());
      documento.setText(parts[2].trim());
      telefono.setText(parts[3].trim());
      disponibilidad.getSelectionModel().select(parts[4].trim());
      zona.setText(parts[5].trim());
    }
  }

  private String parseId(String line) {
    return line.split(" \\|")[0].trim();
  }

  private void clearFields() {
    nombre.clear();
    documento.clear();
    telefono.clear();
    zona.clear();
    if (!disponibilidad.getItems().isEmpty()) disponibilidad.getSelectionModel().select(0);
  }

  private boolean isBlank(String s) {
    return s == null || s.isBlank();
  }
}
