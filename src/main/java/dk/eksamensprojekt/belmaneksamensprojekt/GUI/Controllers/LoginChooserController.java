package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LoginChooserController extends Controller{
    @FXML
    private void loginWithEmail(ActionEvent actionEvent) {
        getInvoker().executeCommand(new SwitchWindowCommand(Windows.LoginEmailWindow));
    }

    @FXML
    private void loginWithChip(ActionEvent actionEvent) {
        getInvoker().executeCommand(new SwitchWindowCommand(Windows.LoginChipWindow));
    }
}
