package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.*;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.Approved;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.ImagePosition;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.UserActions;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.LogCreatorHelper;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;

// JavaFX
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

// Java
import java.net.URL;
import java.util.EnumSet;
import java.util.ResourceBundle;

// Statiske imports
import static dk.eksamensprojekt.belmaneksamensprojekt.BE.Image.IMAGES_PATH;
import static dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts.*;

/**
 * Denne kontroller håndterer billede-fotografering for operatøren.
 */
public class PhotoDocumentController extends Controller implements Initializable {
    private final static int COLUMNS = 5;
    private OrderModel model;
    private Order currentOrder;
    private GridPane scollGridPane;

    //
    // JavaFX komponenter
    //
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

    /**
     * Genindlæser billederne, som vises på vinduet.
     */
    private void refreshGrids() {
        scollGridPane.getChildren().clear();
        resetGridPane();

        int col = 0;
        int row = 0;

        // gennemløber alle billederne
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

    /**
     * Tilføjer et billede til det nederste pane (på vinduet).
     * @param img Billede som skal tilføjes.
     * @param col Hvilken kolonne den skal være på.
     * @param row Hvilken rækker den skal være på.
     */
    private void addImageToExtras(Image img, int col, int row) {
        ImageView imageView = new ImageView(new javafx.scene.image.Image("file:\\" + IMAGES_PATH + img.getPath()));
        imageView.setFitWidth(250);
        imageView.setFitHeight(195);
        imageView.setPreserveRatio(true);

        StackPane pane = createDeleteButton(img, imageView, scollGridPane);

        scollGridPane.add(pane, col, row);
    }


    /**
     * Nulstiller den øverste billevisning (på vinduet)
     */
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

    /**
     * Laver slet-knap til et billede
     * @param img Det billede som skal have en slet-knap
     * @param imageView Det view hvor billedet ligger på
     * @param grid Den placering som billedet er på (øverste eller nederste pane)
     * @return Et konstrueret {@link StackPane} med slet-knap på
     */
    private StackPane createDeleteButton(Image img, ImageView imageView, GridPane grid) {
        // laver knappen
        Button deleteButton = new Button("X");
        deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        deleteButton.setVisible(false);

        // laver et stackPane med billedet og slet-knappen
        StackPane pane = new StackPane(imageView, deleteButton);
        pane.setPrefSize(150, 150);
        pane.setAlignment(Pos.TOP_LEFT);
        if (img.isApproved() == Approved.NOT_APPROVED)
            pane.getStyleClass().add("notApproved");
        else
            pane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        StackPane.setAlignment(deleteButton, Pos.TOP_RIGHT);
        StackPane.setMargin(deleteButton, new Insets(10));

        // tilføjer en aktion når man trykker på "billedet"
        pane.setOnMouseClicked(e -> {
            for (Node node : grid.getChildren()) {
                if (node instanceof StackPane sp && sp.getChildren().size() > 1) {
                    sp.getChildren().get(1).setVisible(false);
                }
            }
            deleteButton.setVisible(true);
        });

        // når man trykker på slet knappen
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

    /**
     * Tjekker om man kan tilføje et billede
     */
    private boolean canAddPicture() {
        return currentOrder.getImageList().size() < currentOrder.getType().getMax();
    }

    /**
     * Tjekker om man har nok billeder
     */
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

        return currentOrder.getImageList().size() >= currentOrder.getType().getMin();
    }

    @FXML
    private void takePictureClicked(ActionEvent event) throws Exception {
        if (!canAddPicture()) {
            displayError("Can't add picture!", "Max Pictures Already");
            return;
        }

        //Image image = new Image(-1, "WIN_20250516_11_55_30_Pro.jpg", Approved.NOT_REVIEWED, ModelManager.INSTANCE.getUserModel().getSelectedUser().get(), currentOrder.getId(), getNextImageLocation());
        Image image = model.takePictureClicked(getNextImageLocation());
        image.setOrderId(currentOrder.getId());
        currentOrder.getImageList().add(image);
        model.saveButtonClicked();
        ModelManager.INSTANCE.getLogModel().createLog(LogCreatorHelper.logFactory(UserActions.TAKE_PICTURE));
        refreshGrids();
    }

    /**
     * Henter den næste {@link ImagePosition} som et billede skal have (når man tager et billede)
     */
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

        ImageView imageView = (ImageView)pane.getChildren().getFirst();
        javafx.scene.image.Image image = new javafx.scene.image.Image("file:\\" + IMAGES_PATH + imageBE.getPath());
        imageView.setImage(image);
    }

    private void promptUserDeleteImage(Image img) throws Exception {
        img.setOrderId(0);
        model.saveButtonClicked();
        currentOrder.getImageList().remove(img);
        ModelManager.INSTANCE.getLogModel().createLog(LogCreatorHelper.logFactory(UserActions.DELETE_PICTURE));
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
