package dk.eksamensprojekt.belmaneksamensprojekt.GUI;

import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

import java.util.List;
import java.util.Map;

/**
 * Interface som gør det muligt for en kontroller at resize.
 */
public interface IController {
    // gemme komponenter som skal kunne resizes.
    void initializeComponents(Pane pane, double width, double height);
    // resize komponenterne gemt på kontrolleren.
    void resizeItems(double width, double height);
    // denne metode bruges often kun af kontrolleren selv, da det er den som har adgang til komponenterne (de ligger i kontrolleren).
    void resizeItems(Map<Region, List<Double>> components, Map<ImageView, List<Double>> imageComponents, double width, double height);
    // hent en fonten -> bruges ofte af kontrolleren selv.
    Font getFont(ObservableList<String> style, double newWidth, double newHeight);
}
