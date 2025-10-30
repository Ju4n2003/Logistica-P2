package co.edu.uniquindio.logistica;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Punto de entrada JavaFX de la plataforma de logÃ­stica. Carga la vista principal {@code
 * logistics_main.fxml} y muestra la escena.
 */
public class LogisticsApplication extends Application {
  /**
   * Inicializa y muestra la ventana principal de la aplicaciÃ³n.
   *
   * @param stage escenario principal provisto por JavaFX
   * @throws Exception si falla la carga del recurso FXML
   */
  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader =
        new FXMLLoader(LogisticsApplication.class.getResource("/logistics/logistics_main.fxml"));
    Scene scene = new Scene(loader.load());
    stage.setTitle("Plataforma de LogÃ­stica");
    stage.setScene(scene);
    stage.show();
  }

  /**
   * MÃ©todo principal para lanzar la aplicaciÃ³n JavaFX.
   *
   * @param args argumentos de lÃ­nea de comandos
   */
  public static void main(String[] args) {
    launch(args);
  }
}
