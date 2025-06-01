package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Services;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers.MainWindowController;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.MainWindowViews;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import dk.eksamensprojekt.belmaneksamensprojekt.Main;

// JavaFX
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

// Java
import java.io.IOException;

/**
 * Denne service fokuserer på at skifte viewet inde i main-vinduet (altså små vinduer).
 */
public class MainWindowService {
    // Denne service kræver main-vinduet -> dette gør a kommandoerne ikke skal kende til main-kontrolleren
    private MainWindowController mwc;

    public MainWindowService(MainWindowController mwc) {
        this.mwc = mwc;
    }

    /**
     * Sætter et view på main-vinduet.
     * @param mainWView Dét {@link MainWindowViews} som skal vises.
     */
    public void setView(MainWindowViews mainWView) {
        try {
            // Load vinduet
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(mainWView.getPath()));
            AnchorPane ap = fxmlLoader.load();
            // sæt vinduet ind på main-vinduet.
            mwc.setView(ap);
        } catch (IOException e) {
            ShowAlerts.displayError("Window Error", "Could not find view!");
        }
    }
}
