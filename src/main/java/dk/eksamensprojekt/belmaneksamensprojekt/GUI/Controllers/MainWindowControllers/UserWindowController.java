package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers.MainWindowControllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.UserRole;
import dk.eksamensprojekt.belmaneksamensprojekt.Constants.Constants;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.UserModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.BackgroundTask;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import dk.eksamensprojekt.belmaneksamensprojekt.Main;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class UserWindowController implements Initializable {

    private UserModel usermodel;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usermodel = ModelManager.INSTANCE.getUserModel();
        initializeSplitMenuButton();
        initializeUsers();
    }

    private void initializeSplitMenuButton() {
        btnsplitMenu.getItems().clear();

        for (UserRole role : UserRole.values()) {
            MenuItem item = new MenuItem(role.toString());
            item.setOnAction(event -> btnsplitMenu.setText(role.toString()));
            btnsplitMenu.getItems().add(item);
        }
    }

    private SplitMenuButton constructSplitMenuButton() {
        SplitMenuButton smb = new SplitMenuButton();
        smb.getItems().clear();

        for (UserRole role : UserRole.values()) {
            MenuItem item = new MenuItem(role.toString());
            item.setOnAction(event -> {
                smb.setText(role.toString());
                smb.fire();
                System.out.println("Fire in the hole");
            });
            smb.getItems().add(item);
        }
        return smb;
    }

    private void initializeUsers() {
        tblUsers.getItems().clear();
        BackgroundTask.<ObservableList<User>>execute(
                ()-> {
                    try {
                        return ModelManager.INSTANCE.getUserModel().getUsers();
                    }
                    catch (Exception e) {
                        ShowAlerts.displayMessage("Database error", e.getMessage(), Alert.AlertType.ERROR);
                    }
                    return FXCollections.emptyObservableList();
                },
                users -> {
                    Platform.runLater(() -> initTableCols());
                    tblUsers.setItems(users);

                },
                error -> {
                    ShowAlerts.displayMessage("Database error", error.getMessage(), Alert.AlertType.ERROR);
                },
                onLoading -> {
                    tblUsers.setPlaceholder(new Label("Loading..."));
                }

        );
    }


    private void initTableCols() {
        colFullName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        colEmail.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));
        colRole.setCellFactory(new Callback<TableColumn<User, SplitMenuButton>, TableCell<User, SplitMenuButton>>() {
            @Override
            public TableCell<User, SplitMenuButton> call(TableColumn<User, SplitMenuButton> param) {
                return new TableCell<>(){
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
                };
            }
        });
    }
    @FXML
    private void BrowsefilesClicked(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
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
            Constants.DisplayError("Update error", e.getMessage());
        }
    }
}
