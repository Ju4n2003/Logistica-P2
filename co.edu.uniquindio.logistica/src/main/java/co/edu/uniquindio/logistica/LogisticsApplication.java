package co.edu.uniquindio.logistica;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Punto de entrada JavaFX de la plataforma de logi­stica. Carga la vista principal {@code
 * logistics_main.fxml} y muestra la escena.
 */
public class LogisticsApplication extends Application {
  /**
   * Inicializa y muestra la ventana principal de la aplicacion.
   *
   * @param stage escenario principal provisto por JavaFX
   * @throws Exception si falla la carga del recurso FXML
   */
  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader loader =
        new FXMLLoader(LogisticsApplication.class.getResource("/logistics/logistics_main.fxml"));
    Scene scene = new Scene(loader.load());
    stage.setTitle("Plataforma de Logistica");
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Metodo principal para lanzar la aplicacion JavaFX.
   *
   * @param args argumentos de linea de comandos
   */
  public static void main(String[] args) {
    launch(args);
  }
}
