package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.UserRole;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.UserModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class TopBarController extends Controller implements Initializable {
    private UserModel userModel;
    private OrderModel orderModel;
    @FXML
    private Label lblName;
    @FXML
    private Label lblRole;
    @FXML
    private Label lblCurrentOrder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // indsæt hvem der er logget ind
        userModel = ModelManager.INSTANCE.getUserModel();
        lblName.setText(userModel.getSelectedUser().get().getName());
        lblRole.setText(userModel.getSelectedUser().get().getRole().toString());

        // nuværende ordre
        orderModel = ModelManager.INSTANCE.getOrderModel();
        lblCurrentOrder.setText(orderModel.getCurrentOrder() == null ? "" : orderModel.getCurrentOrder().getOrderNumber());
    }

    @FXML
    private void logoutPressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            // nulstil current order
            orderModel.setCurrentOrder(null);
            // do logout
            userModel.setSelectedUser(null); // måske ik?
            getInvoker().executeCommand(new SwitchWindowCommand(Windows.LoginWindow));
        }
    }

    // back metode
    @FXML
    private void backPressed(MouseEvent mouseEvent) {
        /*
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            // nulstil current order
            orderModel.setCurrentOrder(null);
            // do back
            if (userModel.getSelectedUser().get().getRole() == UserRole.OPERATOR) {
                getInvoker().executeCommand(new SwitchWindowCommand(Windows.OperatorWindow));
            }
            else{
                getInvoker().executeCommand(new SwitchWindowCommand(Windows.MainWindow));
            }
        }
         */
        getInvoker().undoLastCommand();
    }
}
