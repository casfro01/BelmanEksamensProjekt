package dk.eksamensprojekt.belmaneksamensprojekt.GUI;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.Command;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import dk.eksamensprojekt.belmaneksamensprojekt.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class WindowService {
    private Stage rootStage;
    private Controller currentController;

    public WindowService(Stage rootStage) {
        setRootStage(rootStage);
    }

    public void setRootStage(Stage rootStage) {
        // opsætningen af hovedvinduet
        this.rootStage = rootStage;
        initRootWindow();
    }

    private void initRootWindow(){
        this.rootStage.setWidth(1920);
        this.rootStage.setHeight(1080);
        this.rootStage.setTitle("Belsign");

        setPane(Windows.LoginWindow);

        // add listeners
        rootStage.widthProperty().addListener((observable, oldValue, newValue) -> {currentController.resizeItems(newValue.doubleValue(), rootStage.getHeight());});
        rootStage.heightProperty().addListener((observable, oldValue, newValue) -> {currentController.resizeItems(rootStage.getWidth(), newValue.doubleValue());});
        rootStage.setMaximized(true);
    }

    public void setPane(Windows window) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(window.getPath()));
            Scene scene = new Scene(fxmlLoader.load(), rootStage.getWidth(), rootStage.getHeight());
            rootStage.setScene(scene);
            rootStage.show();
            currentController = fxmlLoader.getController();

            // hvis hovedpanelet er et anchorpane så skal vi resize eller forbliver den bare som den er
            Parent parent = scene.getRoot();
            if (parent instanceof AnchorPane ap){
                // alt andet end loginvinduet skal have topbar
                if (!window.getName().toLowerCase().contains("login")){
                    AnchorPane topbar = getTopBar();
                    ap.getChildren().add(topbar);
                    topbar.setLayoutX(10);
                    topbar.setLayoutY(10);
                }
                currentController.initializeComponents(ap,1920, 1080);
            }
            else
                return;

            currentController.resizeItems(rootStage.getWidth(), rootStage.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
            ShowAlerts.displayMessage("Window Error", "Unable to load window: " + window.getName(), Alert.AlertType.ERROR);
        }
    }

    private AnchorPane getTopBar() throws Exception{
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("topbar.fxml"));
            fxmlLoader.load();
            Controller c = fxmlLoader.getController();

            return fxmlLoader.getRoot();
        } catch (Exception e) {
            throw new Exception("Could not load topbar", e);
        }
    }
}
