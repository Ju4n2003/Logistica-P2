package co.edu.uniquindio.logistica.viewController;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * Controlador principal de navegaciÃ³n para la plataforma de logÃ­stica. Carga dinÃ¡micamente vistas
 * de usuario y administrador en el contenedor central.
 */
public class LogisticsMainController {
  @FXML private StackPane content;

  /** Abre la vista de login/registro de usuario. */
  @FXML
  private void openUser(ActionEvent e) throws IOException {
    loadIntoCenter("/logistics/user_login.fxml");
  }

  /** Abre el panel principal de administraciÃ³n. */
  @FXML
  private void openAdmin(ActionEvent e) throws IOException {
    if (co.edu.uniquindio.logistica.Session.isAdmin()) {
      loadIntoCenter("/logistics/admin_dashboard.fxml");
    } else {
      loadIntoCenter("/logistics/admin_login.fxml");
    }
  }

  /** Abre la pantalla de perfil y direcciones del usuario. */
  @FXML
  private void openProfile(ActionEvent e) throws IOException {
    loadIntoCenter("/logistics/user_profile.fxml");
  }

  /** Abre la pantalla de cotizaciÃ³n de envÃ­os. */
  @FXML
  private void openQuote(ActionEvent e) throws IOException {
    loadIntoCenter("/logistics/user_quote.fxml");
  }

  /** Abre la pantalla de historial y pagos de envÃ­os del usuario. */
  @FXML
  private void openShipments(ActionEvent e) throws IOException {
    loadIntoCenter("/logistics/user_shipments.fxml");
  }

  /**
   * Carga un recurso FXML en el contenedor central.
   *
   * @param resource ruta del recurso FXML
   */
  private void loadIntoCenter(String resource) throws IOException {
    Node node = FXMLLoader.load(getClass().getResource(resource));
    content.getChildren().setAll(node);
  }

  /** Cierra la sesiÃ³n actual y vuelve al mensaje inicial. */
  @FXML
  private void logout() {
    co.edu.uniquindio.logistica.Session.logout();
    content
        .getChildren()
        .setAll(new javafx.scene.control.Label("Selecciona Usuario o Admin para comenzar."));
  }
}
