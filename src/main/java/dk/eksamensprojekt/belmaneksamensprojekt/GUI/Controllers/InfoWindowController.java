package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

// JavaFx
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

// Java
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Denne kontroller håndterer et popup vinduet, som viser noget valgfri information.
 */
public class InfoWindowController implements Initializable {
    @FXML
    private VBox root;

    /**
     * Sætter det som skal vises på vinduet.
     * @param lines Valgfri antal linjer, repræsenteret som {@link String}, der skal vises .
     */
    public void setContent(String ... lines){
        for (String s : lines){
            Label content = new Label(s);
            content.getStyleClass().add("info-window-text");
            root.getChildren().add(content);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        root.setPadding(new Insets(0, 20, 0, 20));
    }
}
