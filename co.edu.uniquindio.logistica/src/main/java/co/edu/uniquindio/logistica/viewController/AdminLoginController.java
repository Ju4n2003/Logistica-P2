package co.edu.uniquindio.logistica.viewController;

import co.edu.uniquindio.logistica.Session;
import co.edu.uniquindio.logistica.repository.InMemoryStore;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/** Login de Administrador. Valida credenciales contra InMemoryStore.credentials. */
public class AdminLoginController {

  @FXML private VBox root;
  @FXML private TextField correo;
  @FXML private PasswordField password;
  @FXML private Label status;

  private final InMemoryStore store = InMemoryStore.getInstance();

  @FXML
  private void onLogin() throws IOException {
    String c = correo.getText();
    String p = password.getText();
    if (c == null || c.isBlank() || p == null || p.isBlank()) {
      status.setText("Ingrese correo y contrasena");
      return;
    }
    String saved = store.credentials().get(c);
    if (saved != null && saved.equals(p) && c.equals("admin@logistica.com")) {
      Session.loginAsAdmin();
      // Navegar al dashboard
      Node node = FXMLLoader.load(getClass().getResource("/logistics/admin_dashboard.fxml"));
      // Reemplazar contenido del parent (root estÃ¡ dentro de StackPane)
      root.getParent().lookupAll("*"); 
      // root.getScene() puede ser null en tests; asumiendo UI activa:
      ((javafx.scene.layout.StackPane) root.getParent()).getChildren().setAll(node);
    } else {
      status.setText("Credenciales invalidas");
    }
  }
}
