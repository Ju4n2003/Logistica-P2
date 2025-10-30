package co.edu.uniquindio.logistica.viewController;

import co.edu.uniquindio.logistica.dto.UsuarioDTO;
import co.edu.uniquindio.logistica.repository.InMemoryStore;
import co.edu.uniquindio.logistica.service.IAdminService;
import co.edu.uniquindio.logistica.service.impl.AdminServiceImpl;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AdminUsersController {

  @FXML private TextField nombre;
  @FXML private TextField correo;
  @FXML private TextField telefono;
  @FXML private javafx.scene.control.PasswordField password;
  @FXML private ListView<String> lista;
  @FXML private Label status;

  private final IAdminService adminService = new AdminServiceImpl();
  private final InMemoryStore store = InMemoryStore.getInstance();

  @FXML
  private void initialize() {
    onRefresh();
    lista
        .getSelectionModel()
        .selectedItemProperty()
        .addListener((obs, o, n) -> loadFromSelection(n));
  }

  @FXML
  private void onCreate() {
    String n = nombre.getText();
    String c = correo.getText();
    String t = telefono.getText();
    if (isBlank(n) || isBlank(c)) {
      status.setText("Nombre y correo son obligatorios");
      return;
    }
    UsuarioDTO dto = new UsuarioDTO(null, n, c, t);
    UsuarioDTO creado = adminService.createUsuario(dto);
    status.setText(creado != null ? ("Creado: " + creado.idUsuario()) : "Error al crear");
    // Si se ingresÃ³ contraseÃ±a, crear credencial
    String p = password != null ? password.getText() : null;
    if (creado != null && p != null && !p.isBlank()) {
      store.credentials().put(creado.correo(), p);
      status.setText(status.getText() + " | Clave establecida");
    }
    clearFields();
    onRefresh();
  }

  @FXML
  private void onUpdate() {
    String sel = lista.getSelectionModel().getSelectedItem();
    if (sel == null) {
      status.setText("Seleccione un usuario");
      return;
    }
    String id = parseId(sel);
    UsuarioDTO dto = new UsuarioDTO(id, nombre.getText(), correo.getText(), telefono.getText());
    UsuarioDTO upd = adminService.updateUsuario(dto);
    status.setText(upd != null ? ("Actualizado: " + upd.idUsuario()) : "No encontrado");
    onRefresh();
  }

  @FXML
  private void onDelete() {
    String sel = lista.getSelectionModel().getSelectedItem();
    if (sel == null) {
      status.setText("Seleccione un usuario");
      return;
    }
    String id = parseId(sel);
    adminService.deleteUsuario(id);
    status.setText("Eliminado: " + id);
    clearFields();
    onRefresh();
  }

  @FXML
  private void onRefresh() {
    lista.getItems().clear();
    List<UsuarioDTO> users = adminService.listUsuarios();
    for (UsuarioDTO u : users) {
      lista
          .getItems()
          .add(
              u.idUsuario()
                  + " | "
                  + u.nombreCompleto()
                  + " | "
                  + u.correo()
                  + " | "
                  + u.telefono());
    }
    status.setText("Usuarios: " + users.size());
  }

  @FXML
  private void onSetPassword() {
    String c = correo.getText();
    String p = password != null ? password.getText() : null;
    if (isBlank(c) || isBlank(p)) {
      status.setText("Ingrese correo y contraseÃ±a");
      return;
    }
    // Verificar que el correo exista en la lista de usuarios
    boolean exists = lista.getItems().stream().anyMatch(line -> line.contains(" | " + c + " | "));
    if (!exists) {
      status.setText("Correo no corresponde a un usuario listado");
      return;
    }
    store.credentials().put(c, p);
    status.setText("ContraseÃ±a establecida para " + c);
    if (password != null) password.clear();
  }

  private void loadFromSelection(String line) {
    if (line == null) return;
    String[] parts = line.split(" \\| ");
    // parts: [id, nombre, correo, telefono]
    if (parts.length >= 4) {
      nombre.setText(parts[1].trim());
      correo.setText(parts[2].trim());
      telefono.setText(parts[3].trim());
    }
  }

  private String parseId(String line) {
    return line.split(" \\|")[0].trim();
  }

  private void clearFields() {
    nombre.clear();
    correo.clear();
    telefono.clear();
  }

  private boolean isBlank(String s) {
    return s == null || s.isBlank();
  }
}
