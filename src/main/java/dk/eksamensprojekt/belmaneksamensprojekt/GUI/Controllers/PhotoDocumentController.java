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
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

import static dk.eksamensprojekt.belmaneksamensprojekt.Constants.Constants.*;

public class PhotoDocumentController extends Controller implements Initializable {
    private final static int COLUMNS = 4;
    private OrderModel model;
    private Order currentOrder;
    private GridPane scollGridPane;

    @FXML
    private GridPane gridPaneAngles;

    @FXML
    private ScrollPane extraImagesPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ModelManager modelManager = ModelManager.INSTANCE;
        model = modelManager.getOrderModel();
        currentOrder = model.getCurrentOrder();

        scollGridPane = new GridPane();
        scollGridPane.setHgap(10);
        scollGridPane.setVgap(10);
        scollGridPane.setPadding(new Insets(10));
        scollGridPane.setAlignment(Pos.TOP_LEFT);

        extraImagesPane.setFitToWidth(true);
        extraImagesPane.setContent(scollGridPane);

        refreshGrids();
    }

    private void refreshGrids() {
        scollGridPane.getChildren().clear();
        resetGridPane();

        int col = 0;
        int row = 0;

        for (Image img : currentOrder.getImageList()) {
            if (img.getImagePosition() != ImagePosition.EXTRA) {
                addImageToGrid(img);
            } else {
                col++;
                if (col >= COLUMNS) {
                    col = 0;
                    row++;
                }
                addImageToExtras(img, col, row);
            }
        }
    }

    private void addImageToExtras(Image img, int col, int row) {
        ImageView imageView = new ImageView(new javafx.scene.image.Image("file:\\" + IMAGES_PATH + img.getPath()));
        imageView.setFitWidth(230);
        imageView.setFitHeight(230);
        imageView.setPreserveRatio(false);

        StackPane pane = createDeleteButton(img, imageView, scollGridPane);

        scollGridPane.add(pane, col, row);
    }


    private void resetGridPane() {
        for (Node node : gridPaneAngles.getChildren()) {
            ImageView imageView = getFirstImageView((VBox) node);
            if (imageView != null) {
                imageView.setImage(null);
            } else {
                System.out.println("Couldnt find imageview when resetting gridpane");
            }
        }
    }

    private StackPane createDeleteButton(Image img, ImageView imageView, GridPane grid) {
        Button deleteButton = new Button("X");
        deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        deleteButton.setVisible(false);

        StackPane pane = new StackPane(imageView, deleteButton);
        pane.setPrefSize(150, 150);
        if (img.isApproved() == Approved.NOT_APPROVED)
            pane.getStyleClass().add("notApproved");
        else
            pane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        StackPane.setAlignment(deleteButton, Pos.TOP_RIGHT);
        StackPane.setMargin(deleteButton, new Insets(10));

        pane.setOnMouseClicked(e -> {
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
            } finally {
                pane.getChildren().remove(deleteButton);
                grid.getChildren().remove(pane);
            }
        });

        return pane;
    }

    @FXML
    private void takePictureClicked(ActionEvent event) throws Exception {
        Image image = new Image(-1, "WIN_20250516_11_55_30_Pro.jpg", Approved.NOT_APPROVED, ModelManager.INSTANCE.getUserModel().getSelectedUser().get(), currentOrder.getId(), getNextImageLocation());
        //Image image = model.takePictureClicked(getNextImageLocation());
        image.setOrderId(currentOrder.getId());
        currentOrder.getImageList().add(image);
        model.saveButtonClicked();
        refreshGrids();
    }

    private ImagePosition getNextImageLocation() {
        int i = 0;
        for (Node node : gridPaneAngles.getChildren()) {
            i+= 1;
            ImageView imageView = getFirstImageView((VBox)node);
            if (imageView != null) {
                if (imageView.getImage() == null) {
                    return ImagePosition.fromInt(i);
                }
            }
        }

        return ImagePosition.EXTRA;
    }

    @FXML
    private void submitButtonClicked() throws Exception {
        model.getCurrentOrder().setApproved(Approved.NOT_REVIEWED); // resetter dens status, da nye ting er kommet frem.
        model.submitButtonClicked();
        ShowAlerts.splashMessage("Submit", "Submitting order...", DISPLAY_TIME);
        backToMain();
    }

    private void addImageToGrid(Image imageBE) {
        VBox vbox = (VBox) gridPaneAngles.getChildren().get(imageBE.getImagePosition().toInt() - 1); // - 1 fordi Extra forskyder det
        StackPane pane;
        if (vbox.getChildren().getFirst() instanceof ImageView) { // delete button isnt setup
            pane = createDeleteButton(imageBE, (ImageView)vbox.getChildren().getFirst(), gridPaneAngles);
            vbox.getChildren().add(pane);
        } else {
            pane = (StackPane) vbox.getChildren().getFirst();
        }

        System.out.println("adding image to grid");
        ImageView imageView = (ImageView)pane.getChildren().getFirst();
        javafx.scene.image.Image image = new javafx.scene.image.Image("file:\\" + IMAGES_PATH + imageBE.getPath());
        imageView.setImage(image);
    }

    private void promptUserDeleteImage(Image img) throws Exception {
        System.out.println("pressed delete -> removing image");
        img.setOrderId(0);
        model.saveButtonClicked();
        currentOrder.getImageList().remove(img);
        refreshGrids();
    }

    private ImageView getFirstImageView(Parent grid) {
        for (Node child : grid.getChildrenUnmodifiable()) {
            if (child instanceof ImageView) {
                return (ImageView) child;
            } else if (child instanceof Parent) {
                ImageView result = getFirstImageView((Parent) child);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    private void backToMain(){
        getInvoker().executeCommand(new SwitchWindowCommand(Windows.OperatorWindow));
    }
}
