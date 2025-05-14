package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.OrderType;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import javafx.fxml.FXML;

public class SmallOrBigOrderController {
    @FXML
    private void chooseLarge() {
        ModelManager.getInstance().getOrderModel().setCurrentOrderType(OrderType.Large);
    }

    @FXML
    private void chooseSmall() {
        ModelManager.getInstance().getOrderModel().setCurrentOrderType(OrderType.Small);
    }
}
