package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.UserRole;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.UserModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.BackgroundTask;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserWindowController extends Controller implements Initializable {

@FXML
private SplitMenuButton btnsplitMenu;
@FXML
private TableView tblUsers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    private void initializeUsers() {
        tblUsers.getItems().clear();
        BackgroundTask.<ObservableList<User>>execute(
                ()-> {
                    try {
                        return ModelManager.getInstance().getUserModel().getUsers();
                    }
                    catch (Exception e) {
                        ShowAlerts.displayMessage("Database error", e.getMessage(), Alert.AlertType.ERROR);
                    }
                    return FXCollections.emptyObservableList();
                },
                users -> {
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
}
