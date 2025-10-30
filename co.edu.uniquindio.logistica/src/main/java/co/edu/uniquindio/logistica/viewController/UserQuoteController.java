package co.edu.uniquindio.logistica.viewController;

import co.edu.uniquindio.logistica.Session;
import co.edu.uniquindio.logistica.dto.EnvioDTO;
import co.edu.uniquindio.logistica.dto.TarifaDTO;
import co.edu.uniquindio.logistica.dto.UsuarioDTO;
import co.edu.uniquindio.logistica.model.Direccion;
import co.edu.uniquindio.logistica.repository.InMemoryStore;
import co.edu.uniquindio.logistica.service.IUserService;
import co.edu.uniquindio.logistica.service.impl.UserServiceImpl;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class UserQuoteController {

  @FXML private ComboBox<Option> origen;
  @FXML private ComboBox<Option> destino;
  @FXML private TextField peso;
  @FXML private TextField volumen;
  @FXML private CheckBox prioridad;
  @FXML private TextArea resultado;
  @FXML private Label status;

  private final IUserService userService = new UserServiceImpl();
  private final InMemoryStore store = InMemoryStore.getInstance();
  private TarifaDTO lastQuote;

  public static class Option {
    public final String id;
    public final String label;

    public Option(String id, String label) {
      this.id = id;
      this.label = label;
    }

    @Override
    public String toString() {
      return label;
    }
  }

  @FXML
  private void initialize() {
    cargarDireccionesUsuario();
  }

  private void cargarDireccionesUsuario() {
    UsuarioDTO u = Session.getCurrentUser();
    if (u == null) {
      status.setText("Inicia sesiÃ³n para cotizar");
      return;
    }
    var user = store.usuarios().get(u.idUsuario());
    if (user == null) return;
    List<Option> opts = new ArrayList<>();
    for (Direccion d : user.getDirecciones()) {
      String label = d.getAlias() + " | " + d.getCalle() + " | " + d.getCiudad();
      opts.add(new Option(d.getIdDireccion(), label));
    }
    origen.getItems().setAll(opts);
    destino.getItems().setAll(opts);
    if (!opts.isEmpty()) {
      origen.getSelectionModel().select(0);
      destino.getSelectionModel().select(0);
    }
  }

  @FXML
  private void onQuote() {
    try {
      var o = origen.getSelectionModel().getSelectedItem();
      var d = destino.getSelectionModel().getSelectedItem();
      double p = Double.parseDouble(peso.getText());
      double v = Double.parseDouble(volumen.getText());
      boolean pr = prioridad.isSelected();
      if (o == null || d == null) {
        status.setText("Selecciona origen y destino");
        return;
      }
      lastQuote = userService.quote(o.id, d.id, p, v, pr);
      resultado.setText(
          "Base: "
              + lastQuote.base()
              + "\n"
              + "Peso: "
              + lastQuote.peso()
              + "\n"
              + "Volumen: "
              + lastQuote.volumen()
              + "\n"
              + "Prioridad: "
              + lastQuote.prioridad()
              + "\n"
              + "Recargos: "
              + lastQuote.recargos()
              + "\n"
              + "Total: "
              + lastQuote.total());
      status.setText("CotizaciÃ³n lista");
    } catch (NumberFormatException ex) {
      status.setText("Peso/Volumen invÃ¡lidos");
    }
  }

  @FXML
  private void onCreate() {
    try {
      var o = origen.getSelectionModel().getSelectedItem();
      var d = destino.getSelectionModel().getSelectedItem();
      double p = Double.parseDouble(peso.getText());
      double v = Double.parseDouble(volumen.getText());
      if (o == null || d == null) {
        status.setText("Selecciona origen y destino");
        return;
      }
      if (lastQuote == null) {
        onQuote();
      }
      UsuarioDTO u = Session.getCurrentUser();
      if (u == null) {
        status.setText("No autenticado");
        return;
      }
      EnvioDTO created =
          userService.createEnvio(
              new EnvioDTO(
                  null,
                  o.id,
                  d.id,
                  p,
                  v,
                  lastQuote != null ? lastQuote.total() : 0,
                  "SOLICITADO",
                  null,
                  null,
                  null,
                  u.idUsuario(),
                  java.util.Set.of()));
      status.setText("EnvÃ­o creado: " + created.idEnvio());
    } catch (NumberFormatException ex) {
      status.setText("Peso/Volumen invÃ¡lidos");
    }
  }
}
