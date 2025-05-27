package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.OrderType;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import static dk.eksamensprojekt.belmaneksamensprojekt.GUI.Providers.InvokerProvider.getInvoker;

@Deprecated
public class SmallOrBigOrderController  extends Controller implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void chooseLarge() {
        //ModelManager.INSTANCE.getOrderModel().setCurrentOrderType(OrderType.Large);
        getInvoker().executeCommand(new SwitchWindowCommand(Windows.PhotoDocWindow));
    }

    @FXML
    private void chooseSmall() {
        //ModelManager.INSTANCE.getOrderModel().setCurrentOrderType(OrderType.Small);
        getInvoker().executeCommand(new SwitchWindowCommand(Windows.PhotoDocWindow));
    }
}
