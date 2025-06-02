package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Services;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import dk.eksamensprojekt.belmaneksamensprojekt.Main;

// JavaFX imports
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Denne service har til formål at håndtere de vinduer som bliver vist i programmet (de "store" vinduer).
 * Desuden holder den også styr på hvilken kontroller er i brug, og at komponenterne på vinduet resizer -
 * som de skal.
 */
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

    /**
     * Opstiller hvordan base vinduet skal se ud.
     */
    private void initRootWindow(){
        this.rootStage.setWidth(1920);
        this.rootStage.setHeight(1080);
        this.rootStage.setTitle("Belsign");

        setPane(Windows.LoginWindow);

        // tilføj listeners
        rootStage.widthProperty().addListener((observable, oldValue, newValue) -> {currentController.resizeItems(newValue.doubleValue(), rootStage.getHeight());});
        rootStage.heightProperty().addListener((observable, oldValue, newValue) -> {currentController.resizeItems(rootStage.getWidth(), newValue.doubleValue());});
        rootStage.setMaximized(true);
    }

    /**
     * Skift vindue.
     * @param window Det nye {@link Windows} som skal vises.
     */
    public void setPane(Windows window) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(window.getPath()));
            Scene scene = new Scene(fxmlLoader.load(), rootStage.getWidth(), rootStage.getHeight());
            rootStage.setScene(scene);
            rootStage.show();
            currentController = fxmlLoader.getController();

            // hvis hovedpanelet er et AnchorPane så skal vi resize ellers forbliver den bare som den er
            Parent parent = scene.getRoot();
            if (parent instanceof AnchorPane ap){
                // alt andet end login-vinduet skal have topbar
                if (window != Windows.LoginWindow){
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
            ShowAlerts.displayError("Window Error", "Unable to load window: " + window.getName());
        }
    }

    /**
     * Hent topbaren.
     * @return Et {@link AnchorPane} som består af forskellige komponenter - der udgør topbaren.
     */
    private AnchorPane getTopBar() throws Exception{
        try {
            // Load filen fra fxml vinduer
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("topbar.fxml"));
            fxmlLoader.load();
            //Controller c = fxmlLoader.getController();

            return fxmlLoader.getRoot();
        } catch (Exception e) {
            throw new Exception("Could not load topbar", e);
        }
    }
}
