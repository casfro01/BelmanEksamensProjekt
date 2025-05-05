package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class OperatorWindowController extends Controller implements Initializable {
    private static final String regex = "^\\d{3}-\\d{5}-\\d{3}-\\d$";
    private Pattern pattern;
    private ModelManager modelManager;
    private OrderModel orderModel;

    @FXML
    private TextField txtSearchOrdernumb;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pattern = Pattern.compile(regex);
        modelManager = ModelManager.getInstance();
        orderModel = modelManager.getOrderModel();
    }

    @FXML
    private void onEnterPressed(KeyEvent keyEvent) throws Exception {
        if (keyEvent.getCode() == KeyCode.ENTER){
           searchOrder();
        }
    }

    @FXML
    private void onSearchPressed() throws Exception {
        // TODO: Forbind search knap tryk til den her metode
        searchOrder();
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
}
