package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.*;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class PhotoDocumentController extends Controller implements Initializable {
    private ModelManager modelManager;
    private OrderModel model;
    private Order currentOrder;

    @FXML
    private ScrollPane imagesScrollPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modelManager = ModelManager.getInstance();
        model = modelManager.getOrderModel();
        currentOrder = model.getCurrentOrder();

        initializeScrollPane();
    }

    private void initializeScrollPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.TOP_LEFT);

        imagesScrollPane.setFitToWidth(true);
        imagesScrollPane.setContent(grid);

        Runnable updateGrid = () -> {
            grid.getChildren().clear();
            int columns = 4;
            int row = 0;
            int col = 0;

            for (Image img : currentOrder.getImageList()) {
                javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(new javafx.scene.image.Image(img.getPath()));
                imageView.setFitWidth(230);
                imageView.setFitHeight(230);
                imageView.setPreserveRatio(false);

                Button deleteButton = new Button("X");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                deleteButton.setVisible(false);

                StackPane imagePane = new StackPane(imageView, deleteButton);
                imagePane.setPrefSize(150, 150);
                imagePane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
                StackPane.setAlignment(deleteButton, Pos.TOP_RIGHT);
                StackPane.setMargin(deleteButton, new Insets(10));

                imagePane.setOnMouseClicked(e -> {
                    for (Node node : grid.getChildren()) {
                        if (node instanceof StackPane sp && sp.getChildren().size() > 1) {
                            sp.getChildren().get(1).setVisible(false);
                        }
                    }
                    deleteButton.setVisible(true);
                });

                deleteButton.setOnAction(e -> {
                    try {
                        promptUserDeleteImage(img);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });

                grid.add(imagePane, col, row);
                col++;
                if (col >= columns) {
                    col = 0;
                    row++;
                }
            }
        };

        updateGrid.run();
        currentOrder.getImageList().addListener((ListChangeListener<Image>) change -> updateGrid.run());
    }

    private void promptUserDeleteImage(Image img) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            img.setOrderId(0);
            save();
            currentOrder.getImageList().remove(img);
        }
    }

    @FXML
    private void takePictureClicked(ActionEvent event) throws Exception {
        model.takePictureClicked();
    }

    @FXML
    private void addPictureClicked(ActionEvent event) throws Exception {
        model.addPictureClicked();
    }

    @FXML
    private void saveButtonClicked(ActionEvent event) throws Exception {
        save();
    }

    @FXML
    private void submitButtonClicked(ActionEvent event) throws Exception {
        model.submitButtonClicked();
    }

    private void save() throws Exception {
        model.saveButtonClicked();
    }
}
