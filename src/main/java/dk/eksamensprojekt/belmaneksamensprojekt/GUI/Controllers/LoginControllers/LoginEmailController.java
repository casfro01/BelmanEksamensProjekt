package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers.LoginControllers;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.UserRole;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.UserModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;

// JavaFX
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

// Java
import java.net.URL;
import java.util.ResourceBundle;

public class LoginEmailController extends Controller implements Initializable {
    private UserModel userModel;

    //
    // JavaFX komponenter
    //
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userModel = ModelManager.INSTANCE.getUserModel();
    }

    @FXML
    private void loginIn(ActionEvent actionEvent) {
        // TODO : integrér LoginModel i stedet
        try {
            // logger brugeren ind
            User u = userModel.login(txtUsername.getText(), txtPassword.getText());
            if (u != null) {
                loginAsUser(u);
            }
            else
                throw new Exception("Wrong email or password!");
        } catch (Exception e) {
            // TODO : refactor til noget andet - så som et error label
            ShowAlerts.displayError("Login Error", "Could not login: " + e.getMessage());
        }
    }

    private void loginAsUser(User user){
        userModel.setSelectedUser(user);
        // hvis operator gå her
        if (user.getRole() == UserRole.OPERATOR){
            getInvoker().executeCommand(new SwitchWindowCommand(Windows.OperatorWindow));
        }
        // hvis andet gå til main; admin el. qc-user
        else{
            getInvoker().executeCommand(new SwitchWindowCommand(Windows.MainWindow));
        }
    }
}
