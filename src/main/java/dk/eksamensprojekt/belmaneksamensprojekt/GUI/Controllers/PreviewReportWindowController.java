package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Image;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class PreviewReportWindowController extends Controller implements Initializable {
    private ModelManager modelManager;
    private OrderModel model;

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
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.TOP_LEFT);

        CheckboxscrollPane.setFitToWidth(true);
        CheckboxscrollPane.setContent(grid);

        grid.getChildren().clear();
        int columns = 4;
        int row = 0;
        int col = 0;

        for (Image image : model.getCurrentOrder().getImageList()) {
            javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(new javafx.scene.image.Image(image.getPath()));
            imageView.setFitWidth(230);
            imageView.setFitHeight(230);
            imageView.setPreserveRatio(false);

            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(true);

            StackPane imagePane = new StackPane(imageView, checkBox);
            imagePane.setPrefSize(150, 150);
            imagePane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
            StackPane.setAlignment(checkBox, Pos.TOP_RIGHT);
            StackPane.setMargin(checkBox, new Insets(10));

            grid.add(imagePane, col, row);
            col++;
            if (col >= columns) {
                col = 0;
                row++;
            }


        }

    }

}
