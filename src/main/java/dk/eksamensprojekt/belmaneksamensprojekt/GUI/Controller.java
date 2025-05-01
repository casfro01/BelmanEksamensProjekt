package dk.eksamensprojekt.belmaneksamensprojekt.GUI;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Region;

import java.util.*;

import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class Controller implements IController {
    private Map<Region, List<Double>> windowItems = new HashMap<>();
    private Map<ImageView, List<Double>> imageViews = new HashMap<>();
    private IController root;
    private WindowInvoker invoker;

    public void initializeComponents(AnchorPane pane, double width, double height) {
        for (Node n : pane.getChildren()) {
            if (n instanceof AnchorPane a){
                initializeComponents(a, width, height);
            }
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

    /*
    public Map<Region, List<Double>> getWindowItems() {
        return windowItems;
    }

    public Map<ImageView, List<Double>> getImageViews() {
        return imageViews;
    }
    public IController getRoot(){
        return root
    }
     */

    @Override
    public void resizeItems(double width, double height) {
        resizeItems(windowItems, imageViews, width, height);
    }

    @Override
    public void resizeItems(Map<Region, List<Double>> components, Map<ImageView, List<Double>> imageComponents, double width, double height){
        width -= 15; // hold dig fra siden mand!
        height -= 30;
        for (Region n : components.keySet()) {
            //n.resize(width * windowItems.get(n).get(0), height * windowItems.get(n).get(1));
            System.out.println(n instanceof Button ? n.getLayoutY() : 0);
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

    @Override
    public Font getFont(ObservableList<String> style, double newWidth, double newHeight){
        double orgSize = style.contains("bigText") ? 32 : style.contains("normalText") ? 24 : 16;
        double newValueAVG = (orgSize * (newWidth / 1920) + orgSize * (newHeight / 1080)) / 2;
        return new Font(newValueAVG);
    }

    @Override
    public void setControllerRoot(IController controller) {
        root = controller;
    }

    /**
     * Henter invoker - bruges primært i child-klasserne
     */
    private WindowInvoker getInvoker() {
        return invoker;
    }

}
