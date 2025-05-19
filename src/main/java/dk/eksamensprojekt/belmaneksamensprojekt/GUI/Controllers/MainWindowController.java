package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Approved;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.UserRole;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchMainView;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Services.MainWindowService;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.BackgroundTask;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.MainWindowViews;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import dk.eksamensprojekt.belmaneksamensprojekt.Main;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindowController extends Controller implements Initializable {

    private OrderModel orderModel;
    private MainWindowService mainWindowService;

    @FXML
    private Label lblUser;
    @FXML
    private Label lblLogs;
    @FXML
    private AnchorPane showPane;
    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderModel = ModelManager.INSTANCE.getOrderModel();
        mainWindowService = new MainWindowService(this);

        // TODO : måske en bedre fiks
        orderModel.setCurrentOrder(null);

        isAdmin();
    }

    private void isAdmin() {
        if (ModelManager.INSTANCE.getUserModel().getSelectedUser().getValue().getRole() == UserRole.ADMIN){
            lblUser.setVisible(true);
            lblLogs.setVisible(true);
            Platform.runLater(() -> loadAllOrders(null));
        }
        else{
            Platform.runLater(() -> showOrderForApproval(null));
        }
    }

    private void openDocumentWindow(Order order){
        orderModel.setCurrentOrder(order);
        getInvoker().executeCommand(new SwitchWindowCommand(Windows.PreviewPicturesWindow));
    }

    @FXML
    private void manageUsers(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            getInvoker().executeCommand(new SwitchMainView(mainWindowService, MainWindowViews.UserWindow));
        }
    }

    @FXML
    private void manageLogs(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            getInvoker().executeCommand(new SwitchMainView(mainWindowService, MainWindowViews.AdminLogWindow));
        }
    }

    public void setView(AnchorPane anchorPane){
        showPane.getChildren().clear();
        showPane.getChildren().add(anchorPane);

        // oprindelige størrelse
        double width = 1920;
        double height = 1080;
        // tilføj hvad der er i showpanet
        initializeComponents(showPane, width, height);
        resizeItems(Stage.getWindows().getFirst().getWidth(), Stage.getWindows().getFirst().getHeight());
    }

    @FXML
    private void loadAllOrders(MouseEvent mouseEvent) {
        getInvoker().executeCommand(new SwitchMainView(mainWindowService, MainWindowViews.AllOrdersWindow));
    }

    @FXML
    private void showOrderForApproval(MouseEvent mouseEvent) {
        getInvoker().executeCommand(new SwitchMainView(mainWindowService, MainWindowViews.OrderForApprovalWindow));
    }
}
