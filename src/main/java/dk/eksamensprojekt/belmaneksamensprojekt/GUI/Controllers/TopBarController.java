package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.UserRole;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.UserModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class TopBarController extends Controller implements Initializable {
    private UserModel userModel;
    @FXML
    private Label lblName;
    @FXML
    private Label lblRole;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // indsæt hvem der er logget ind
        userModel = ModelManager.getInstance().getUserModel();
        lblName.setText(userModel.getSelectedUser().get().getName());
        lblRole.setText(userModel.getSelectedUser().get().getRole().toString().toLowerCase());
    }

    @FXML
    private void logoutPressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            // do logout
            userModel.setSelectedUser(null); // måske ik?
            getInvoker().executeCommand(new SwitchWindowCommand(Windows.LoginWindow));
        }
    }

    // back metode
    // TODO : lav på anden måde
    @FXML
    private void backPressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            // do logout
            if (userModel.getSelectedUser().get().getRole() == UserRole.OPERATOR) {
                getInvoker().executeCommand(new SwitchWindowCommand(Windows.OperatorWindow));
            }
            else{
                getInvoker().executeCommand(new SwitchWindowCommand(Windows.MainWindow));
            }
        }
    }
}
