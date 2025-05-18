package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Services;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers.MainWindowController;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.MainWindowViews;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import dk.eksamensprojekt.belmaneksamensprojekt.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainWindowService {
    private MainWindowController mwc;

    public MainWindowService(MainWindowController mwc) {
        this.mwc = mwc;
    }

    public void setView(MainWindowViews mainWView) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(mainWView.getPath()));
            AnchorPane ap = fxmlLoader.load();
            mwc.setView(ap);
        } catch (IOException e) {
            ShowAlerts.displayMessage("Window Error", "Could not find view!", Alert.AlertType.ERROR);
        }
    }
}
