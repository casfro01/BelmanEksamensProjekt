package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers.MainWindowControllers;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.LoginUser;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.UserRole;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.UserModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.BackgroundTask;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;

// JavaFX
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

// Java
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

// Statiske imports
import static dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts.displayError;
import static dk.eksamensprojekt.belmaneksamensprojekt.BE.Image.IMAGES_PATH;

/**
 * Denne kontroller håndterer bruger-vinduet på main-vinduet.
 * Her kan brugerne (admin-brugeren) lave- og ændre brugere.
 */
public class UserWindowController implements Initializable {
    private UserModel usermodel;

    //
    // JavaFx komponenter
    //
    @FXML
    private SplitMenuButton btnsplitMenu;
    @FXML
    private TableView<User> tblUsers;
    @FXML
    private TableColumn<User, String> colFullName;
    @FXML
    private TableColumn<User, String> colEmail;
    @FXML
    private TableColumn<User, SplitMenuButton> colRole;
    @FXML
    private ImageView imgUploadLogo;
    @FXML
    private Label lblUploadPhoto;
    @FXML
    private Label lblFeedback;
    @FXML
    private TextField txtFullName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usermodel = ModelManager.INSTANCE.getUserModel();
        initializeSplitMenuButton();
        initializeUsers();
    }

    /**
     * Indsætter roller i en {@link SplitMenuButton} på vinduet.
     */
    private void initializeSplitMenuButton() {
        btnsplitMenu.getItems().clear();
        // gennemløber alle roller som findes i programmet
        for (UserRole role : UserRole.values()) {
            MenuItem item = new MenuItem(role.toString());
            item.setOnAction(event -> btnsplitMenu.setText(role.toString()));
            btnsplitMenu.getItems().add(item);
        }
    }

    /**
     * Laver en {@link SplitMenuButton} med brugerroller.
     * Ken evt. bruges til at ændre brugerens rolle dynamisk -> når en rolle bliver valgt -
     * så vil den automatisk opdatere den brugers rolle.
     */
    private SplitMenuButton constructSplitMenuButton() {
        SplitMenuButton smb = new SplitMenuButton();
        smb.getItems().clear();

        // gennemløber brugerroller i programmet
        for (UserRole role : UserRole.values()) {
            MenuItem item = new MenuItem(role.toString());
            // når man har valgt en rolle, så skifter knappen tekst og programmet trykker på knappen for brugeren
            item.setOnAction(event -> {
                smb.setText(role.toString());
                smb.fire(); // trykker kunstigt på knappen.
            });
            smb.getItems().add(item);
        }
        return smb;
    }

    private void initializeUsers() {
        tblUsers.getItems().clear();
        BackgroundTask.<ObservableList<User>>execute(
                ()-> { // hvad skal der gøres
                    try {
                        return ModelManager.INSTANCE.getUserModel().getUsers();
                    }
                    catch (Exception e) {
                        throw new Error(e);
                    }
                },
                users -> { // hvis det lykkes
                    Platform.runLater(this::initTableCols);
                    tblUsers.setItems(users);

                },
                error -> { // hvis det fejler
                    ShowAlerts.displayError("Database error", error.getMessage());
                    tblUsers.setPlaceholder(new Label("Try again later."));
                },
                // når den loader
                _ -> tblUsers.setPlaceholder(new Label("Loading..."))

        );
    }

    /**
     * Klargøre tableviewets kolonner.
     */
    private void initTableCols() {
        colFullName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        colEmail.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));
        // "indsæt" SplitMenuButton
        colRole.setCellFactory(param -> new TableCell<>(){
            protected void updateItem(SplitMenuButton item, boolean empty) {
                if (empty)
                    setGraphic(null);
                else{
                    SplitMenuButton smb = constructSplitMenuButton();
                    User user = getTableView().getItems().get(getIndex());
                    smb.setOnAction(event -> changeUser(user, UserRole.valueOfString(smb.getText())));
                    smb.setText(user.getRole().toString());
                    setGraphic(smb);
                }
            }
        });
    }

    /**
     * Åben stifinder.
     */
    @FXML
    private void BrowsefilesClicked(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        // Sæt initial directory til vores billede mappe -> som simulerer deres "folder"
        File initialDir = new File(IMAGES_PATH);
        if (initialDir.exists() && initialDir.isDirectory())
            fileChooser.setInitialDirectory(initialDir);

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            // sæt billede
            javafx.scene.image.Image image = new javafx.scene.image.Image(file.toURI().toString());
            imgUploadLogo.setImage(image);
            lblUploadPhoto.setVisible(false);
        }
    }

    private void changeUser(User user, UserRole role) {
        try{
            user.setRole(role);
            usermodel.updateUser(user);
        } catch (Exception e) {
            displayError("Update error", e.getMessage());
        }
    }

    @FXML
    private void createUser(ActionEvent actionEvent) {
        final int STYLE_CLASS_POS = 1; // Positionen af feedback farven... måske en anden løsning kunne være bedre
        try {
            // konstruere bruger objekter
            User baseUser = new User(-1, UserRole.valueOfString(btnsplitMenu.getText()), txtEmail.getText().trim(), txtFullName.getText().trim());
            if (imgUploadLogo.getImage() != null) {
                String[] url = imgUploadLogo.getImage().getUrl().split("/");
                baseUser.setImagePath(url[url.length - 1]); // sæt profilbillede -> hvis valgt
            }

            LoginUser loginUser = new LoginUser(-1, txtEmail.getText().trim(), txtPassword.getText().trim());

            // send til database
            usermodel.createUser(baseUser, loginUser);

            // opdatér feedbacken
            lblFeedback.setText("User created!");
            lblFeedback.getStyleClass().set(STYLE_CLASS_POS, "greenText");
        } catch (Exception e) {
            // opdatér feedbacken
            lblFeedback.setText("Unable to create user: " + e.getMessage());
            lblFeedback.getStyleClass().set(STYLE_CLASS_POS, "redText");
        }
    }
}
