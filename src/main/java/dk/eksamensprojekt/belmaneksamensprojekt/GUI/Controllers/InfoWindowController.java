package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class InfoWindowController implements Initializable {
    @FXML
    private VBox root;

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
