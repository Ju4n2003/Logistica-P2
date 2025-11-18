module co.edu.uniquindio.logistica {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  requires org.apache.pdfbox;

  // Paquetes definitivos en logistica
  opens co.edu.uniquindio.logistica.viewController to
      javafx.fxml;

  exports co.edu.uniquindio.logistica.viewController;
  exports co.edu.uniquindio.logistica;
  exports co.edu.uniquindio.logistica.dto;
}
