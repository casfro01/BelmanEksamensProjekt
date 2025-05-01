package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginWindowController extends Controller implements Initializable {

    @FXML private ScrollPane scrollPaneUser;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillOutUsers();
    }

    private void fillOutUsers() {
        // henter brugere
        List<User> users;
        try{
            //users = ModelManager.getInstance().getUserModel().getUsers();
        } catch (Exception e) {
            ShowAlerts.displayMessage("Database Error", "Could not fetch users: " + e.getMessage(), Alert.AlertType.ERROR);
            return;
        }
        // hent achorpane
        scrollPaneUser.getChildrenUnmodifiable().clear();
        AnchorPane ap = new AnchorPane();
        // i think
        ap.setPrefSize(ScrollPane.USE_COMPUTED_SIZE, ScrollPane.USE_COMPUTED_SIZE);

        /** TODO remove things in this box
        * under here
        */
        users = new ArrayList<>();
        users.add(new User(11, 3, "Jens@Jensen.dk", "Jens Jensen"));
        users.add(new User(12, 2, "Jens@Jensen1.dk", "Jens Jensen1"));
        users.add(new User(13, 1, "Jens@Jensen2.dk", "Jens Jensen2"));
        /** ends here */
        int counter = 0;
        int spacing = 65;
        for (User u : users){
            AnchorPane userPane = createUserPane(u);
            ap.getChildren().add(userPane);
            userPane.setLayoutX(0);
            userPane.setLayoutY(spacing * counter);
            counter++;
        }

        scrollPaneUser.setContent(ap);
    }

    private AnchorPane createUserPane(User user) {
        AnchorPane ap = new AnchorPane(); // TODO : custom klasse som har en bruger når man klikker på den?
        ap.getStyleClass().add("userPane");

        int spacing = 10;
        // tilføj pfp
        ImageView iv = new ImageView();
        ap.getChildren().add(iv);
        iv.setFitHeight(48);
        iv.setFitWidth(48);
        /*
        // TODO : giv brugere et pfp
        javafx.scene.image.Image image = new javafx.scene.image.Image(String.valueOf(new File("Images/image1.jpg")));
        iv.setImage(image);
        iv.setX(spacing);
        iv.setY(spacing);
        
         */

        // navn - overskrift
        Label name = new Label(user.getName());
        ap.getChildren().add(name);
        name.setLayoutX(iv.getLayoutX() + iv.getFitWidth() + spacing);
        name.setLayoutY(spacing);
        // rolle
        Label role = new Label(user.getRole().toString());
        ap.getChildren().add(role);
        role.setLayoutX(iv.getLayoutX() + iv.getFitWidth() + spacing);
        role.setLayoutY(name.getLayoutY() + spacing);
        return ap;
    }
}
