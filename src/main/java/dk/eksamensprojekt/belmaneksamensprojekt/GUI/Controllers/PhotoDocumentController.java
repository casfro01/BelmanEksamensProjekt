package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.*;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import javafx.collections.ListChangeListener;
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
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import static dk.eksamensprojekt.belmaneksamensprojekt.Constants.Constants.*;

public class PhotoDocumentController extends Controller implements Initializable {
    private ModelManager modelManager;
    private OrderModel model;
    private Order currentOrder;

    @FXML
    private Label guideLabel;

    @FXML
    private ScrollPane imagesScrollPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        guideLabel.getStyleClass().add("bigText");
        modelManager = ModelManager.INSTANCE;
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
                ImageView imageView = new ImageView(new javafx.scene.image.Image("file:\\" + IMAGES_PATH + img.getPath()));
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

                deleteButton.setOnAction(_ -> {
                    try {
                        promptUserDeleteImage(img);
                    } catch (Exception ex) {
                        DisplayError("Error", ex);
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

    @FXML
    private void takePictureClicked(ActionEvent event) throws Exception {
        currentOrder.getImageList().add(model.takePictureClicked());
        model.saveButtonClicked();
    }

    @FXML
    private void submitButtonClicked() throws Exception {
        model.getCurrentOrder().setApproved(Approved.NOT_REVIEWED); // resetter dens status, da nye ting er kommet frem.
        model.submitButtonClicked();
        ShowAlerts.splashMessage("Submit", "Submitting order...", DISPLAY_TIME);
        backToMain();
    }

    private void promptUserDeleteImage(Image img) throws Exception {
        img.setOrderId(0);
        model.saveButtonClicked();
        currentOrder.getImageList().remove(img);
        ShowAlerts.splashMessage("Deletion", "Image deleted", DISPLAY_TIME);
    }

    private void backToMain(){
        getInvoker().executeCommand(new SwitchWindowCommand(Windows.OperatorWindow));
    }
}
