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
import dk.eksamensprojekt.belmaneksamensprojekt.Main;

// JavaFX
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

// Java
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

// Statiske imports
import static dk.eksamensprojekt.belmaneksamensprojekt.BE.Image.IMAGES_PATH;

/**
 * Denne kontroller håndtere brugerens "chip-login" når de "scanner" deres id/badge ...
 * / Det er denne klasse om håndtere når brugeren vælger hvem de vil logge ind som.
 */
public class LoginChipController extends Controller implements Initializable {
    private UserModel userModel;

    //
    // JavaFX komponenter
    //
    @FXML
    private ScrollPane scrollPaneUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userModel = ModelManager.INSTANCE.getUserModel();
        fillOutUsers();
    }


    /**
     * Udfyldning af brugere i vinduet på scrollpanet.
     */
    private void fillOutUsers() {
        // henter brugere
        List<User> users;
        try{
            users = userModel.getUsers();
        } catch (Exception e) {
            ShowAlerts.displayError("Database Error", "Could not fetch users: " + e.getMessage());
            return;
        }
        // hent anchorPane
        scrollPaneUser.getChildrenUnmodifiable().clear();
        AnchorPane ap = new AnchorPane();
        // Sætter str. på ap
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
            userPane.setPrefWidth(scrollPaneUser.getPrefWidth() - spacing * 3);
            userPane.setPrefHeight(spacing + 5);
            userPane.setLayoutY((10 + spacing) * counter);
            // sætter en metode som man kan bruge når man skal logge ind
            userPane.setOnMouseClicked(_ -> loginAsUser(u));
            userPane.setCursor(Cursor.HAND);
            // Hvilket element som man er nået til - så afstanden bliver dynamisk ift. mængden af elementer
            counter++;
        }

        scrollPaneUser.setContent(ap);
    }

    private AnchorPane createUserPane(User user) {
        // TODO : custom klasse el.lign. som har en bruger når man klikker på den? - eller man kan lave en metode ... måske et factory?
        AnchorPane ap = new AnchorPane();
        ap.getStyleClass().add("userPane");

        int spacing = 10;
        // tilføj pfp
        ImageView iv = new ImageView();
        ap.getChildren().add(iv);
        iv.setFitHeight(48);
        iv.setFitWidth(48);
        iv.setPreserveRatio(false);

        // tilføj user pfp
        String path = user.getImagePath().equals(User.getBasicUserImage()) ? String.valueOf(Main.class.getResource(User.getBasicUserImage())) : "file:\\" + IMAGES_PATH + user.getImagePath();
        iv.setImage(new javafx.scene.image.Image(path));
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
        Label role = new Label(user.getRole().toString());
        ap.getChildren().add(role);
        role.getStyleClass().add("userPaneText");
        role.setLayoutX(iv.getLayoutX() + iv.getFitWidth() + spacing * 2);
        role.setLayoutY(name.getLayoutY() + 10 + spacing);
        return ap;
    }


    private void loginAsUser(User user){
        // sætter den bruger, som brugeren har valgt.
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
}
