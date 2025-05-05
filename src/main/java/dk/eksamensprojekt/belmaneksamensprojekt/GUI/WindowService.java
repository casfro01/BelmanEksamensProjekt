package dk.eksamensprojekt.belmaneksamensprojekt.GUI;

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

public class WindowService {
    private Stage rootStage;
    private Controller currentController;
    public WindowService(Stage rootStage) {
        // opsætningen af hovedvinduet
        this.rootStage = rootStage;
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
            // set invoker
            // hvis / når det er man skal kunne undo, så skal dette nedenfor laves om, da der laves en ny invoker
            currentController.setInvoker(new WindowInvoker(this));

            // hvis hovedpanelet er et anchorpane så skal vi resize eller forbliver den bare som den er
            Parent parent = scene.getRoot();
            if (parent instanceof AnchorPane ap){
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
}
