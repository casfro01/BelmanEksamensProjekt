package dk.eksamensprojekt.belmaneksamensprojekt.GUI;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Providers.InvokerProvider;

// JavaFX
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

// Java
import java.util.*;

/**
 * Denne abstrakte klasse sørger for at holde styr på resizing.
 * Hvis en kontroller nedarver fra denne, så kan dets komponenter resizes -> alt efter
 * hvad der styrer skiftingen af vinduer -> desuden skal kontrolleren også forbindes til vinduet ->
 * i form af "subscribers" på vinduets resize property.
 */
public abstract class Controller implements IController {
    private Map<Region, List<Double>> windowItems = new HashMap<>();
    private Map<ImageView, List<Double>> imageViews = new HashMap<>();

    /**
     * Gem komponenterne forbundet til vinduet hvori denne kontroller styrer.
     * @param pane Grund panelet, hvorpå der befinder sig komponenter.
     * @param width Base bredden af vinduet.
     * @param height Base højden af vinduet.
     */
    public void initializeComponents(Pane pane, double width, double height) {
        for (Node n : pane.getChildren()) {
            // hent "children" fra andre panes
            if (n instanceof AnchorPane a){
                initializeComponents(a, width, height);
            }
            else if (n instanceof GridPane grid){
                initializeComponents(grid, width, height);
            }
            else if (n instanceof VBox vbox){
                initializeComponents(vbox, width, height);
            }
            else if (n instanceof StackPane stack){
                initializeComponents(stack, width, height);
            }

            // tilføj komponenten til listen
            if (n instanceof Region region) {
                windowItems.put(region, new ArrayList<>(){{
                    add(region.getPrefWidth() / width);
                    add(region.getPrefHeight() / height);
                    add(region.getLayoutX() / width);
                    add(region.getLayoutY() / height);}});
            }
            else if (n instanceof ImageView iv) {
                imageViews.put(iv, new ArrayList<>(){{
                    add(iv.getFitWidth() / width);
                    add(iv.getFitHeight() / height);
                    add(iv.getLayoutX() / width);
                    add(iv.getLayoutY() / height);}});
            }
        }
    }

    /**
     * Resize komponenterne på vinduet til en ny størrelse.
     * @param width Ny bredde.
     * @param height Ny højde
     */
    @Override
    public void resizeItems(double width, double height) {
        resizeItems(windowItems, imageViews, width, height);
    }

    /**
     * Resize komponenterne på vinduet til en ny størrelse.
     * @param components {@link Region} komponenter samlet i et {@link Map}.
     * @param imageComponents {@link ImageView} komponenter samlet i et {@link Map}.
     * @param width Ny bredde.
     * @param height Ny højde.
     */
    @Override
    public void resizeItems(Map<Region, List<Double>> components, Map<ImageView, List<Double>> imageComponents, double width, double height){
        width -= 15; // hold dig fra siden mand!
        height -= 30;
        for (Region n : components.keySet()) {
            //n.resize(width * windowItems.get(n).get(0), height * windowItems.get(n).get(1));
            n.setPrefWidth(width * components.get(n).get(0));
            n.setLayoutX(width * components.get(n).get(2));

            n.setPrefHeight(height * components.get(n).get(1));
            n.setLayoutY(height * components.get(n).get(3));

            // set font size - relative to the size difference
            switch (n) {
                case Label label -> label.setFont(getFont(label.getStyleClass(), width, height));
                case Button btn -> btn.setFont(getFont(btn.getStyleClass(), width, height));
                case TextField txt -> txt.setFont(getFont(txt.getStyleClass(), width, height));
                case TextArea txt -> txt.setFont(getFont(txt.getStyleClass(), width, height));
                case SplitMenuButton smb -> smb.setFont(getFont(smb.getStyleClass(), width, height));
                case DatePicker dp -> dp.setStyle("-fx-font-size:" + getFont(dp.getStyleClass(), width, height).getSize() +"px;");
                default -> {
                }
            }
        }
        if (imageComponents == null || imageComponents.isEmpty())
            return;

        // image views
        for (ImageView v : imageComponents.keySet()){
            v.setFitWidth(width * imageComponents.get(v).get(0));
            v.setFitHeight(height * imageComponents.get(v).get(1));
            v.setLayoutX(width * imageComponents.get(v).get(2));
            v.setLayoutY(height * imageComponents.get(v).get(3));
        }
    }

    /**
     * Resizer fonten (tekst str.) baseret på original størrelsen af vinduet -> teksten altid har
     * samme procentvis størrelse.
     * @param style Det styling som er på det nuværende tekstbaseret objekt.
     * @param newWidth Ny bredde.
     * @param newHeight Ny højde.
     * @return {@link Font} som kan bruges til at resize tekst baseret objekter.
     */
    @Override
    public Font getFont(ObservableList<String> style, double newWidth, double newHeight){
        double orgSize = style.contains("bigText") ? 32 : style.contains("normalText") ? 24 : 16;
        double newValueAVG = (orgSize * (newWidth / 1920) + orgSize * (newHeight / 1080)) / 2;
        return new Font(newValueAVG);
    }

    /**
     * Henter invoker - bruges primært i child-klasserne
     */
    public WindowInvoker getInvoker() {
        return InvokerProvider.getInvoker();
    }
}
