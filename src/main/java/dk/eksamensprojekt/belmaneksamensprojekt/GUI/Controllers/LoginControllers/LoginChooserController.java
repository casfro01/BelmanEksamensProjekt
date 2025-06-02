package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers.LoginControllers;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;

// JavaFX
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

// Java
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Denne kontroller håndtere brugerens valgt om hvilken metode som de vil logge ind med (om det er email eller chip).
 */
public class LoginChooserController extends Controller implements Initializable {

    /**
     * Login med email:
     * Loader login med email vinduet.
     */
    @FXML
    private void loginWithEmail(ActionEvent actionEvent) {
        getInvoker().executeCommand(new SwitchWindowCommand(Windows.LoginEmailWindow));
    }

    /**
     * Login med chip:
     * Loader login med chip vinduet.
     */
    @FXML
    private void loginWithChip(ActionEvent actionEvent) {
        getInvoker().executeCommand(new SwitchWindowCommand(Windows.LoginChipWindow));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO : måske en bedre fiks
        ModelManager.INSTANCE.getOrderModel().setCurrentOrder(null);
    }
}
