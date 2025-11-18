package co.edu.uniquindio.logistica.viewController;

import co.edu.uniquindio.logistica.Session;
import co.edu.uniquindio.logistica.dto.DireccionDTO;
import co.edu.uniquindio.logistica.dto.UsuarioDTO;
import co.edu.uniquindio.logistica.repository.InMemoryStore;
import co.edu.uniquindio.logistica.service.IUserService;
import co.edu.uniquindio.logistica.service.impl.UserServiceImpl;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class UserProfileController {

  @FXML private TextField alias;
  @FXML private TextField calle;
  @FXML private TextField ciudad;
  @FXML private ListView<String> listaDirecciones;
  @FXML private Label status;

  private final IUserService userService = new UserServiceImpl();
  private final InMemoryStore store = InMemoryStore.getInstance();

  @FXML
  private void initialize() {
    refreshList();
  }

  private void refreshList() {
    listaDirecciones.getItems().clear();
    UsuarioDTO u = Session.getCurrentUser();
    if (u == null) {
      status.setText("Inicia sesion para gestionar direcciones");
      return;
    }
    var user = store.usuarios().get(u.idUsuario());
    if (user == null) return;
    listaDirecciones
        .getItems()
        .addAll(
            user.getDirecciones().stream()
                .map(
                    d ->
                        d.getIdDireccion()
                            + " | "
                            + d.getAlias()
                            + " | "
                            + d.getCalle()
                            + " | "
                            + d.getCiudad())
                .collect(Collectors.toList()));
  }

  @FXML
  private void onAddDireccion() {
    UsuarioDTO u = Session.getCurrentUser();
    if (u == null) {
      status.setText("No autenticado");
      return;
    }
    String a = alias.getText();
    String c1 = calle.getText();
    String c2 = ciudad.getText();
    if (a == null || a.isBlank() || c1 == null || c1.isBlank() || c2 == null || c2.isBlank()) {
      status.setText("Complete alias, calle y ciudad");
      return;
    }
    userService.addDireccion(u.idUsuario(), new DireccionDTO(null, a, c1, c2));
    alias.clear();
    calle.clear();
    ciudad.clear();
    refreshList();
    status.setText("Direccion agregada");
  }

  @FXML
  private void onRemoveDireccion() {
    UsuarioDTO u = Session.getCurrentUser();
    if (u == null) {
      status.setText("No autenticado");
      return;
    }
    String selected = listaDirecciones.getSelectionModel().getSelectedItem();
    if (selected == null) return;
    String id = selected.split(" \\|")[0].trim();
    userService.removeDireccion(u.idUsuario(), id);
    refreshList();
    status.setText("Direccion eliminada");
  }
}
