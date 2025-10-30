package co.edu.uniquindio.logistica.viewController;

import co.edu.uniquindio.logistica.Session;
import co.edu.uniquindio.logistica.dto.UsuarioDTO;
import co.edu.uniquindio.logistica.service.IUserService;
import co.edu.uniquindio.logistica.service.impl.UserServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UserLoginController {
  @FXML private TextField correo;
  @FXML private PasswordField password;
  @FXML private Label status;

  private final IUserService userService = new UserServiceImpl();

  @FXML
  private void onLogin() {
    String c = correo.getText();
    String p = password.getText();
    UsuarioDTO u = userService.login(c, p);
    if (u == null) {
      status.setText("Login fallido");
    } else {
      status.setText("Bienvenido, " + u.nombreCompleto());
      Session.setCurrentUser(u);
    }
  }

  @FXML
  private void onRegister() {
    String c = correo.getText();
    String p = password.getText();
    if (c == null || c.isBlank() || p == null || p.isBlank()) {
      status.setText("Ingrese correo y contraseÃ±a");
      return;
    }
    UsuarioDTO nuevo = new UsuarioDTO(null, c, c, "");
    UsuarioDTO creado = userService.register(nuevo, p);
    if (creado != null) {
      status.setText("Registrado: " + creado.idUsuario());
      Session.setCurrentUser(creado);
    }
  }
}
