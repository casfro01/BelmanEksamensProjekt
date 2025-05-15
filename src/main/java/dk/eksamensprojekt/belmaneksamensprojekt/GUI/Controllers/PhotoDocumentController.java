package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.*;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class PhotoDocumentController extends Controller implements Initializable {
    private static final String IMAGES_PATH = System.getProperty("user.dir") + File.separator + "Images" + File.separator;
    private static double DISPLAY_TIME = 1.5;
    private ModelManager modelManager;
    private OrderModel model;
    private Order currentOrder;
    private ObservableList<Image> replicaImageList = FXCollections.observableArrayList();
    private boolean syncingLists = false;

    private static final int SMALL_ORDER_MIN = 5;
    private static final int SMALL_ORDER_MAX = 10;
    private static final int LARGE_ORDER_MIN = 10;
    private static final int LARGE_ORDER_MAX = 100;
    private static final List<String> GUIDANCE_TEXT = new ArrayList<>(Arrays.asList(
            "left", "right", "above", "behind", "the front"
    ));

    @FXML
    private Label guideLabel;

    @FXML
    private ScrollPane imagesScrollPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modelManager = ModelManager.INSTANCE;
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
                javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(new javafx.scene.image.Image("file:\\" + IMAGES_PATH + img.getPath()));
                imageView.setFitWidth(230);
                imageView.setFitHeight(230);
                imageView.setPreserveRatio(false);

                Button deleteButton = new Button("X");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                deleteButton.setVisible(false);

                StackPane imagePane = new StackPane(imageView, deleteButton);
                imagePane.setPrefSize(150, 150);
                if (img.isApproved() == Approved.NOT_APPROVED)
                    imagePane.getStyleClass().add("notApproved");
                else
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

            if (model.getCurrentOrderType() == OrderType.Small) {
                if (grid.getChildren().size() < GUIDANCE_TEXT.size()) {
                    guideLabel.setStyle("-fx-fill: red");
                    guideLabel.setText(grid.getChildren().size() + " / 10"  + " - Take picture from " + GUIDANCE_TEXT.get(grid.getChildren().size()));
                } else {
                    guideLabel.setStyle("-fx-fill: green");
                    guideLabel.setText(grid.getChildren().size() + " / 10"  + " Take extra pictures");
                }
            } else {
                guideLabel.setText(grid.getChildren().size() + " / " + LARGE_ORDER_MAX  + " Take more pictures");

                if (grid.getChildren().size() < LARGE_ORDER_MIN) {
                    guideLabel.setStyle("-fx-fill: red");
                } else {
                    guideLabel.setStyle("-fx-fill: green");
                }
            }
        };

        updateGrid.run();
        replicaImageList.addListener((ListChangeListener<Image>) change -> updateGrid.run());
    }

    private void promptUserDeleteImage(Image img) throws Exception {
        replicaImageList.remove(img);
        ShowAlerts.splashMessage("Deletion", "Image deleted", DISPLAY_TIME);
    }

    @FXML
    private void takePictureClicked(ActionEvent event) throws Exception {
        if (canAddPicture(replicaImageList.size())) {
            try {
                replicaImageList.add(model.takePictureClicked());
                save();
            } catch (Exception ex) {
                ShowAlerts.displayMessage("Couldn't add picture", ex.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            ShowAlerts.displayMessage("Max Pictures", "You can't add more pictures!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void addPictureClicked(ActionEvent event) throws Exception {
        if (canAddPicture(replicaImageList.size())) {
            try {
                replicaImageList.add(model.addPictureClicked());
                save();
            } catch (Exception ex) {
                ShowAlerts.displayMessage("Couldn't add picture", ex.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            ShowAlerts.displayMessage("Max Pictures", "You can't add more pictures!", Alert.AlertType.ERROR);
        }
    }

    private boolean canAddPicture(int pictures) {
        if (model.getCurrentOrderType() == OrderType.Small) {
            return pictures < SMALL_ORDER_MAX;
        } else if (model.getCurrentOrderType() == OrderType.Large) {
            return pictures < LARGE_ORDER_MAX;
        }

        return true;
    }

    @FXML
    private void saveButtonClicked(ActionEvent event) throws Exception {
        save();
        ShowAlerts.splashMessage("Save", "Saving order...", DISPLAY_TIME);
        // back to main?
    }

    @FXML
    private void submitButtonClicked(ActionEvent event) throws Exception {
        model.getCurrentOrder().setApproved(Approved.NOT_REVIEWED); // resetter dens status, da nye ting er kommet frem.
        model.submitButtonClicked();
        ShowAlerts.splashMessage("Submit", "Submitting order...", DISPLAY_TIME);
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
