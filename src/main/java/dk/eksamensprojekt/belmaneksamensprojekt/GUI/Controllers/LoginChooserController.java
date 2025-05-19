package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginChooserController extends Controller implements Initializable {

    @FXML
    private void loginWithEmail(ActionEvent actionEvent) {
        getInvoker().executeCommand(new SwitchWindowCommand(Windows.LoginEmailWindow));
    }

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
