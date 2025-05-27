package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.BackgroundTask;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class OperatorWindowController extends Controller implements Initializable {
    private static final String regex = "^\\d{3}-\\d{5}-\\d{3}-\\d$";
    private Pattern pattern;
    private ModelManager modelManager;
    private OrderModel orderModel;

    @FXML
    private TextField txtSearchOrdernumb;
    @FXML
    private ListView<Order> lstOrders;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pattern = Pattern.compile(regex);
        modelManager = ModelManager.INSTANCE;
        orderModel = modelManager.getOrderModel();

        // TODO : måske en bedre fiks
        orderModel.setCurrentOrder(null);


        initializeListView();
    }

    private void initializeListView() {
        // load liste
        BackgroundTask.execute(
            () -> {
                try {
                    FilteredList<Order> filteredList = new FilteredList<>(orderModel.getOrderList());
                    return filteredList;
                } catch (Exception e) {
                    throw new Error(e);
                }
            },
            orders -> {
                Platform.runLater(() -> {
                    txtSearchOrdernumb.textProperty().addListener((observable, oldValue, newValue) -> {
                        orders.setPredicate(order -> filterOrder(order, newValue));
                    });
                    lstOrders.setItems(orders);});
            },
            error ->{
                Platform.runLater(() -> ShowAlerts.displayMessage("Database error", "Could not fetch orders: " + error.getMessage(), Alert.AlertType.ERROR));
            },
            loading -> {
                Platform.runLater(() ->{
                    lstOrders.setPlaceholder(new Label("Loading orders..."));
                });
            }
        );

        // når man klikker på ordre
        lstOrders.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (lstOrders.getSelectionModel().getSelectedItem() != null){
                orderModel.setCurrentOrder(lstOrders.getSelectionModel().getSelectedItem());
                getInvoker().executeCommand(new SwitchWindowCommand(Windows.PhotoDocWindow));
            }
        });
    }

    private boolean filterOrder(Order order, String query){
        return order.getOrderNumber().startsWith(query);
    }

    @FXML
    private void onEnterPressed(KeyEvent keyEvent) throws Exception {
        if (keyEvent.getCode() == KeyCode.ENTER){
           searchOrder();
        }
    }

    private void searchOrder() throws Exception {
        String txt = txtSearchOrdernumb.getText();
        if (txt.isEmpty()) {
            return;
        }

        if (!pattern.matcher(txt).matches()) {
            throw new Exception("Order id doesnt match regex");
        }

        Order order = orderModel.searchOrder(txt);
        orderModel.setCurrentOrder(order);
        getInvoker().executeCommand(new SwitchWindowCommand(Windows.PhotoDocWindow));
    }

    @FXML
    private void searchOrderPressed(MouseEvent mouseEvent) {
        try {
            searchOrder();
        } catch (Exception e) {
            ShowAlerts.displayMessage("Invalid Order", "Invalid order number " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
