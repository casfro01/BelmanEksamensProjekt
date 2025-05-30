package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.Approved;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.UserActions;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Image;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.LogCreatorHelper;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

import static dk.eksamensprojekt.belmaneksamensprojekt.BE.Image.IMAGES_PATH;
import static dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts.*;

public class PreviewPictures extends Controller implements Initializable {
    OrderModel orderModel;

    private final static int COLUMNS = 2;
    private int row = 0;
    private int col = 0;

    @FXML
    private ScrollPane imageScrollPane;
    @FXML
    private ImageView fullscreenImageView;
    @FXML
    private Button closeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderModel = ModelManager.INSTANCE.getOrderModel();

        closeButton.setVisible(false);
        fullscreenImageView.setVisible(false);

        closeButton.setOnMousePressed(event -> {
            toggleFullScreen();
        });

        fullscreenImageView.setOnMousePressed(event -> {
            toggleFullScreen();
        });

        Platform.runLater(() -> initializeScrollPane(orderModel.getCurrentOrder()));
    }

    private void initializeScrollPane(Order order) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(50);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.TOP_RIGHT);

        grid.getChildren().clear();

        for (Image image : orderModel.getCurrentOrder().getImageList()) {
            addImage(grid, image);
        }

        imageScrollPane.setContent(grid);
    }

    private void toggleFullScreen() {
        if (fullscreenImageView.isVisible()) {
            fullscreenImageView.setVisible(false);
            fullscreenImageView.setImage(null);
            closeButton.setVisible(false);

        } else {
            fullscreenImageView.setVisible(true);
            closeButton.setVisible(true);
        }
    }

    private void addImage(GridPane grid, Image image) {
        javafx.scene.image.Image imageToView = new javafx.scene.image.Image("file:\\" + IMAGES_PATH + image.getPath());
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(imageToView);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(500);
        imageView.setFitHeight(300);

        // lav buttons
        RadioButton rBApproved = new RadioButton("Approve");
        rBApproved.getStyleClass().add("smallText");
        rBApproved.setSelected(image.isApproved() == Approved.APPROVED);

        RadioButton rBReject = new RadioButton("Reject");
        rBReject.getStyleClass().add("smallText");
        rBReject.setSelected(image.isApproved() == Approved.NOT_APPROVED);

        // on action tryk på imageview
        imageView.setOnMousePressed(event -> {
            toggleFullScreen();
            fullscreenImageView.setImage(imageToView);
        });

        // lav on actions til knapperne
        rBApproved.setOnAction(event -> {
            if (rBReject.isSelected()) {
                rBReject.setSelected(false);
            }
            image.setApproved(Approved.APPROVED);
        });
        rBReject.setOnAction(event -> {
            if (rBApproved.isSelected()) {
                rBApproved.setSelected(false);
            }
            image.setApproved(Approved.NOT_APPROVED);
        });

        // lav label som fortæller orientering
        Label lblOrientation = new Label(image.getImagePosition().toString());
        lblOrientation.setFont(new Font(16.0d));
        // lav vbox
        VBox vBox = new VBox(lblOrientation, imageView, rBApproved, rBReject);

        // set placering
        grid.add(vBox, col, row);
        col++;
        if (col >= COLUMNS) {
            col = 0;
            row++;
        }
    }

    @FXML
    private void previewReport(ActionEvent actionEvent) {
        // før man kan lave en rapport, så skal alle billeder være godkendt
        for (Image img : orderModel.getCurrentOrder().getImageList()){
            if (img.isApproved() != Approved.APPROVED){
                displayMessage("Approval Required", "All pictures must be approved before you can generate a report.", Alert.AlertType.WARNING);
                return;
            }
        }
        getInvoker().executeCommand(new SwitchWindowCommand(Windows.PreviewReportWindow));
    }

    @FXML
    private void saveStatus(ActionEvent actionEvent) {
        Order currentOrder = orderModel.getCurrentOrder();
        boolean noImageRejected = true;
        for (Image img : currentOrder.getImageList()){
            // hvis én af billederne ikke er blevet gennemset endnu og man gemmer, så skal ens progression -
            // stadig gemmes -> men ordren er stadig ikke godkendt, men først når alle billeder er godkendt
            if (img.isApproved() == Approved.NOT_REVIEWED){
                try {
                    currentOrder.setApproved(Approved.NOT_REVIEWED);
                    orderModel.saveButtonClicked();
                    ModelManager.INSTANCE.getLogModel().createLog(LogCreatorHelper.logFactory(UserActions.EDIT_ORDER));
                    displayMessage("Saved!", "Status is saved!", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    displayError("Not Saved", e.getMessage());
                }
                return;
            }
            else if (img.isApproved() == Approved.NOT_APPROVED)
                noImageRejected = false;
        }
        currentOrder.setApproved(Approved.valueOfBoolean(noImageRejected));
        currentOrder.setDocumented(noImageRejected); // hvis den er false -> så sendes den tilbage til operatøren
        try {
            orderModel.saveButtonClicked();
            ModelManager.INSTANCE.getLogModel().createLog(LogCreatorHelper.logFactory(noImageRejected ? UserActions.APPROVED_ORDER : UserActions.REJECT_ORDER));
            splashMessage("Update", "Order status saved", DEFAULT_DISPLAY_TIME);
        } catch (Exception e) {
            displayError("Update failed", "Could not save order. Try again later. " + e.getMessage());
        }
    }
}
