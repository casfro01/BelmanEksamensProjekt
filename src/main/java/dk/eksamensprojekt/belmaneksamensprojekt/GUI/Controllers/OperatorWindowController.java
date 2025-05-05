package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class OperatorWindowController extends Controller implements Initializable {
    private ModelManager modelManager;
    private OrderModel orderModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modelManager = ModelManager.getInstance();
        orderModel = modelManager.getOrderModel();
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class OperatorWindowController extends Controller {
    @FXML
    private TextField txtSearchOrdernumb;

    @FXML
    private void onEnterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER){
            // do things
            getInvoker().executeCommand(new SwitchWindowCommand(Windows.PhotoDocWindow));
        }
    }
}
