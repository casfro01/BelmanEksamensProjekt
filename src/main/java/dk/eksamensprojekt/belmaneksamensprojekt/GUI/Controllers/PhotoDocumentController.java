package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.*;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PhotoDocumentController extends Controller implements Initializable {
    private ModelManager modelManager;
    private OrderModel model;
    private Order currentOrder;
    private ObservableList<Image> replicaImageList = FXCollections.observableArrayList();
    private boolean syncingLists = false;

    @FXML
    private ScrollPane imagesScrollPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modelManager = ModelManager.getInstance();
        model = modelManager.getOrderModel();
        currentOrder = model.getCurrentOrder();
        replicaImageList.addAll(currentOrder.getImageList());

        currentOrder.getImageList().addListener((ListChangeListener<Image>) change -> {
            if (syncingLists) return;
            while (change.next()) {
                if (change.wasAdded()) {
                    replicaImageList.addAll(change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    replicaImageList.removeAll(change.getRemoved());
                }
            }
        });

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

            for (Image img : replicaImageList) {
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
        replicaImageList.addListener((ListChangeListener<Image>) change -> updateGrid.run());
    }

    private void promptUserDeleteImage(Image img) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            replicaImageList.remove(img);
        }
    }

    @FXML
    private void takePictureClicked(ActionEvent event) throws Exception {
        replicaImageList.add(model.takePictureClicked());
    }

    @FXML
    private void addPictureClicked(ActionEvent event) throws Exception {
        replicaImageList.add(model.addPictureClicked());
    }

    @FXML
    private void saveButtonClicked(ActionEvent event) throws Exception {
        save();
        // back to main?
    }

    @FXML
    private void submitButtonClicked(ActionEvent event) throws Exception {
        model.submitButtonClicked();
        backToMain();
    }

    private void backToMain(){
        getInvoker().executeCommand(new SwitchWindowCommand(Windows.OperatorWindow));
    }

    private void save() throws Exception {
        syncingLists = true;

        try {
            for (Image img : currentOrder.getImageList()) {
                if (!replicaImageList.contains(img)) {
                    img.setOrderId(0);
                }
            }

            List<Image> copy = new ArrayList<>(replicaImageList);
            for (Image img : copy) {
                if (!currentOrder.getImageList().contains(img)) {
                    img.setOrderId(currentOrder.getId());
                    currentOrder.getImageList().add(img);
                }
            }

            model.saveButtonClicked();
        } finally {
            syncingLists = false;
        }
    }
}
