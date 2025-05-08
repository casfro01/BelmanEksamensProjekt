package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.UserRole;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.UserModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import dk.eksamensprojekt.belmaneksamensprojekt.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class LoginWindowController extends Controller implements Initializable {

    private UserModel userModel;
    private static final String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final Pattern pattern = Pattern.compile(emailRegex);
    @FXML
    private ScrollPane scrollPaneUser;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userModel = ModelManager.getInstance().getUserModel();
        fillOutUsers();
    }

    private void fillOutUsers() {
        // henter brugere
        List<User> users;
        try{
            users = userModel.getUsers();
        } catch (Exception e) {
            ShowAlerts.displayMessage("Database Error", "Could not fetch users: " + e.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        // hent anchorPane
        scrollPaneUser.getChildrenUnmodifiable().clear();
        AnchorPane ap = new AnchorPane();
        // i think - sætter str.
        ap.setPrefSize(ScrollPane.USE_COMPUTED_SIZE, ScrollPane.USE_COMPUTED_SIZE);
        scrollPaneUser.setStyle("-fx-background-color: #F2F2F2;");

        // laver alle brugere som anchorPanes ;)
        int counter = 0;
        int spacing = 65;
        for (User u : users){
            // lav basis pane baseret på brugeren
            AnchorPane userPane = createUserPane(u);
            // tilføj til anchor panet som ligger i scrollpanet
            ap.getChildren().add(userPane);
            // ekstra formatering ift. hvordan den skal ligge
            userPane.setLayoutX(0);
            userPane.setPrefWidth(scrollPaneUser.getPrefWidth() - spacing * 4);
            userPane.setPrefHeight(spacing + 5);
            userPane.setLayoutY((10 + spacing) * counter);
            // sætter en metode som man kan bruge når man skal logge ind
            userPane.setOnMouseClicked(event -> {loginAsUser(u);});
            userPane.setCursor(Cursor.HAND);
            // hvilket element som man er nået til - så afstanden bliver dynamisk ift. mængden af elementer
            counter++;
        }

        scrollPaneUser.setContent(ap);
    }

    private AnchorPane createUserPane(User user) {
        AnchorPane ap = new AnchorPane(); // TODO : custom klasse som har en bruger når man klikker på den? - eller man kan lave en metode
        ap.getStyleClass().add("userPane");

        int spacing = 10;
        // tilføj pfp
        ImageView iv = new ImageView();
        ap.getChildren().add(iv);
        iv.setFitHeight(48);
        iv.setFitWidth(48);
        iv.setPreserveRatio(false);

        // TODO : tilføj user pfp ægte
        iv.setImage(new javafx.scene.image.Image(String.valueOf(Main.class.getResource("Icons/BasicUser.jpg"))));
        iv.setX(spacing);
        iv.setY(spacing);
        iv.setClip(new Circle((iv.getFitWidth() / 2) + iv.getX(), (iv.getFitHeight() / 2) + iv.getY(), 24));

        // navn - overskrift
        Label name = new Label(user.getName());
        ap.getChildren().add(name);
        name.getStyleClass().add("userPaneTitleText");
        name.setLayoutX(iv.getLayoutX() + iv.getFitWidth() + spacing * 2);
        name.setLayoutY(spacing);
        // rolle
        Label role = new Label(user.getRole().toString().toLowerCase());
        ap.getChildren().add(role);
        role.getStyleClass().add("userPaneText");
        role.setLayoutX(iv.getLayoutX() + iv.getFitWidth() + spacing * 2);
        role.setLayoutY(name.getLayoutY() + 10 + spacing);
        return ap;
    }


    private void loginAsUser(User user){
        // TODO : lav
        userModel.setSelectedUser(user);
        // hvis operator gå her
        if (user.getRole() == UserRole.OPERATOR){
            getInvoker().executeCommand(new SwitchWindowCommand(Windows.OperatorWindow));
        }
        // hvis andet gå til main admin el. qc user
        else{
            getInvoker().executeCommand(new SwitchWindowCommand(Windows.MainWindow));
        }
    }


    @FXML
    private void loginIn(ActionEvent actionEvent) {
        try {
            if (pattern.matcher(txtUsername.getText().trim()).matches()) {
                User u = userModel.login(txtUsername.getText(), txtPassword.getText());
                if (u != null) {
                    loginAsUser(u);
                }
                else
                    throw new Exception("Wrong email or password!");
            }
            else
                throw new Exception("Unsupported email!");
        } catch (Exception e) {
            // TODO : refactor til noget andet - så som et error label
            ShowAlerts.displayMessage("Login Error", "Could not login: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
