package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.*;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.Approved;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.ImagePosition;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.EnumSet;
import java.util.ResourceBundle;

import static dk.eksamensprojekt.belmaneksamensprojekt.BE.Image.IMAGES_PATH;
import static dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts.*;

public class PhotoDocumentController extends Controller implements Initializable {
    private final static int COLUMNS = 5;
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
        scollGridPane.setHgap(2);
        scollGridPane.setVgap(5);
        // scollGridPane.setPadding(new Insets(10));
        // scollGridPane.setAlignment(Pos.TOP_LEFT);

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
                addImageToExtras(img, col, row);

                col++;
                if (col >= COLUMNS) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    private void addImageToExtras(Image img, int col, int row) {
        ImageView imageView = new ImageView(new javafx.scene.image.Image("file:\\" + IMAGES_PATH + img.getPath()));
        imageView.setFitWidth(250);
        imageView.setFitHeight(195);
        imageView.setPreserveRatio(true);

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
        pane.setAlignment(Pos.TOP_LEFT);
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
                displayError("Error", ex.getMessage());
                throw new RuntimeException(ex);
            } finally {
                if (img.getImagePosition() != ImagePosition.EXTRA) {
                    VBox box = (VBox) imageView.getParent().getParent();
                    box.getChildren().add(imageView);
                    box.getChildren().remove(pane);
                }
            }
        });

        return pane;
    }

    private boolean canAddPicture() {
        /*
        OrderType type = currentOrder.getType();
        if (type == OrderType.Small) {
            return currentOrder.getImageList().size() < SMALL_ORDER_MAX;
        } else if (type == OrderType.Large) {
            return currentOrder.getImageList().size() < LARGE_ORDER_MAX;
        }

        return true;
         */
        return currentOrder.getImageList().size() < currentOrder.getType().getMax();
    }

    private boolean hasEnoughPictures() {
        EnumSet<ImagePosition> existingEnums = EnumSet.noneOf(ImagePosition.class);
        EnumSet<ImagePosition> requiredEnums = EnumSet.allOf(ImagePosition.class);
        requiredEnums.remove(ImagePosition.EXTRA);

        for (Image img : currentOrder.getImageList()) {
            if (img.getImagePosition() != ImagePosition.EXTRA) {
                existingEnums.add(img.getImagePosition());
            }
        }
        if (!existingEnums.containsAll(requiredEnums)) { // indeholder ikke front, left, right osv
            return false;
        }

        /*
        if (model.getCurrentOrderType() == OrderType.Small) {
            return currentOrder.getImageList().size() >= SMALL_ORDER_MIN;
        } else if (model.getCurrentOrderType() == OrderType.Large) {
            return currentOrder.getImageList().size() >= LARGE_ORDER_MIN;
        }

        return false;
         */
        return currentOrder.getImageList().size() >= currentOrder.getType().getMin();
    }

    @FXML
    private void takePictureClicked(ActionEvent event) throws Exception {
        if (!canAddPicture()) {
            displayError("Can't add picture!", "Max Pictures Already");
            return;
        }

        Image image = new Image(-1, "WIN_20250516_11_55_30_Pro.jpg", Approved.NOT_REVIEWED, ModelManager.INSTANCE.getUserModel().getSelectedUser().get(), currentOrder.getId(), getNextImageLocation());
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
        if (!hasEnoughPictures()) {
            displayError("Can't submit", "Not Enough Pictures");
            return;
        }
        model.getCurrentOrder().setApproved(Approved.NOT_REVIEWED); // resetter dens status, da nye ting er kommet frem.
        model.submitButtonClicked();
        splashMessage("Submit", "Submitting order...", DEFAULT_DISPLAY_TIME);
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
        System.out.println(imageView.getFitWidth());
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
