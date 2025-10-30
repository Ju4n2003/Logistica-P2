package co.edu.uniquindio.logistica.viewController;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * Panel de administracion que navega entre modulos de Admin (Usuarios, Repartidores, Asignaciones e
 * informes de Metricas).
 */
public class AdminDashboardController {
  @FXML private StackPane content;

  @FXML
  private void openUsers() throws IOException {
    load("/logistics/admin_users.fxml");
  }

  @FXML
  private void openDrivers() throws IOException {
    load("/logistics/admin_drivers.fxml");
  }

  @FXML
  private void openAssign() throws IOException {
    load("/logistics/admin_assign.fxml");
  }

  /** Abre el panel de MÃ©tricas de administraciÃ³n. */
  @FXML
  private void openMetrics() throws IOException {
    load("/logistics/admin_metrics.fxml");
  }

  private void load(String res) throws IOException {
    Node node = FXMLLoader.load(getClass().getResource(res));
    content.getChildren().setAll(node);
  }
}
