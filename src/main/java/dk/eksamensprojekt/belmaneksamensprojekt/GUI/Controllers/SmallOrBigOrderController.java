package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;

// JavaFX
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

// Java
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Denne klasse bruges ikke længere... brude slettes.
 * Denne klasse er {@link Deprecated} fordi dette bliver nu gjort et andet sted ->
 * Dette medfører at operatøren ikke selv skal vælge om det er en stor eller lille ordre.
 */
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
