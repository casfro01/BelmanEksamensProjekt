package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Approved;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Image;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PreviewPictures extends Controller implements Initializable {
    OrderModel orderModel;

    private List<Image> images = new ArrayList<>();
    private List<CheckBox> checkboxes = new ArrayList<>();
    private final static int COLUMNS = 2;
    private static final String IMAGES_PATH = System.getProperty("user.dir") + File.separator + "Images" + File.separator;
    private int row = 0;
    private int col = 0;

    @FXML
    private ScrollPane imageScrollPane;
    @FXML
    private RadioButton radNotApproved;
    @FXML
    private RadioButton radApproved;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderModel = ModelManager.INSTANCE.getOrderModel();
        Platform.runLater(() -> initializeScrollPane(orderModel.getCurrentOrder()));
    }

    private void initializeScrollPane(Order order) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(50);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.TOP_RIGHT);

        /*
        CheckboxscrollPane.setFitToWidth(true);
        CheckboxscrollPane.setContent(grid);
         */

        grid.getChildren().clear();

        for (Image image : orderModel.getCurrentOrder().getImageList()) {
            addImage(grid, image);
        }

        imageScrollPane.setContent(grid);
    }

    private void addImage(GridPane grid, Image image) {
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(new javafx.scene.image.Image("file:\\" + IMAGES_PATH + image.getPath()));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(500);
        imageView.setFitHeight(300);

        CheckBox checkBox = new CheckBox("Image approved");
        checkBox.setStyle("-fx-font-size: 16px; -fx-padding: 10px;");
        checkBox.setSelected(image.isApproved() == Approved.NOT_REVIEWED ? true : image.isApproved().toBoolean());
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            image.setApproved(Approved.valueOfBoolean(newValue));
        });

        VBox vBox = new VBox(imageView, checkBox);
        vBox.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        //StackPane.setAlignment(checkBox, Pos.BOTTOM_LEFT);
        StackPane.setMargin(checkBox, new Insets(10));

        checkboxes.add(checkBox);
        images.add(image);

        grid.add(vBox, col, row);
        col++;
        if (col >= COLUMNS) {
            col = 0;
            row++;
        }
    }

    @FXML
    private void previewReport(ActionEvent actionEvent) {
    }

    @FXML
    private void saveStatus(ActionEvent actionEvent) {
        orderModel.getCurrentOrder().setApproved(Approved.valueOfBoolean(radApproved.isSelected()));
        orderModel.getCurrentOrder().setDocumented(radApproved.isSelected());
        try {
            orderModel.getCurrentOrder().getImageList().forEach(img -> System.out.println(img.isApproved()));
            orderModel.saveButtonClicked();
            backToMain();
            ShowAlerts.splashMessage("Update", "Order status saved", 1.5);
        } catch (Exception e) {
            // viser en fejlbesked i 3 sekunder, som fortæller brugeren at opdateringen mislykkes, dog kan det også være at man skal lave den som kræver brugerens interaktion
            ShowAlerts.displayMessage("Update failed", "Could not save order. Try again later. " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void setNotApproved(ActionEvent actionEvent) {
        radNotApproved.setSelected(true);
        if (radNotApproved.isSelected()) {
            radApproved.setSelected(false);
        }
    }

    @FXML
    private void setApproved(ActionEvent actionEvent) {
        radApproved.setSelected(true);
        if (radApproved.isSelected()) {
            radNotApproved.setSelected(false);
        }
    }

    private void backToMain(){
        getInvoker().executeCommand(new SwitchWindowCommand(Windows.MainWindow));
    }
}
