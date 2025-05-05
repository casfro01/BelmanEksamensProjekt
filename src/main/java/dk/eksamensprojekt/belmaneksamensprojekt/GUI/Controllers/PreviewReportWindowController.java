package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.*;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class PreviewReportWindowController extends Controller implements Initializable {
    private final static int COLUMNS = 2;
    private ModelManager modelManager;
    private OrderModel model;
    private int row = 0;
    private int col = 0;

    @FXML
    private ScrollPane CheckboxscrollPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modelManager = ModelManager.getInstance();
        model = modelManager.getOrderModel();

        initializeScrollPane();
    }

    private void initializeScrollPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(50);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.TOP_RIGHT);

        CheckboxscrollPane.setFitToWidth(true);
        CheckboxscrollPane.setContent(grid);

        grid.getChildren().clear();

        int i = 0;
        for (Image image : model.getCurrentOrder().getImageList()) {
            if (i % 2 == 0) {
                addImage(grid, image);
                addTextArea(grid);
            } else {
                addTextArea(grid);
                addImage(grid, image);
            }

            i += 1;
        }

    }

    private void addImage(GridPane grid, Image image) {
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(new javafx.scene.image.Image(image.getPath()));
        imageView.setPreserveRatio(false);
        imageView.setFitWidth(500);
        imageView.setFitHeight(300);

        CheckBox checkBox = new CheckBox();
        checkBox.setStyle("-fx-padding: 10px");
        checkBox.setSelected(true);

        StackPane imagePane = new StackPane(imageView, checkBox);
        imagePane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        StackPane.setAlignment(checkBox, Pos.TOP_RIGHT);
        StackPane.setMargin(checkBox, new Insets(10));

        grid.add(imagePane, col, row);
        col++;
        if (col >= COLUMNS) {
            col = 0;
            row++;
        }
    }

    private void addTextArea(GridPane grid) {
        TextArea textArea = new TextArea();
        textArea.setPrefWidth(500);
        textArea.setPrefHeight(300);
        textArea.getStyleClass().add("textAreaFont");

        grid.add(textArea, col, row);
        col++;
        if (col >= COLUMNS) {
            col = 0;
            row++;
        }
    }
}
