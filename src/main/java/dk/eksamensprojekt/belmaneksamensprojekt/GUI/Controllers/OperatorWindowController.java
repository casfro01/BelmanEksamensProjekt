package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

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
    }
}
