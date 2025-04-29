package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.OrderManager;
import javafx.collections.ObservableList;

import java.io.IOException;

public class OrderModel {
    private OrderManager orderManager;
    private ObservableList<Order> orderList;
    private Order currentOrder;

    public OrderModel() {
        this.orderManager = new OrderManager();
    }

    public ObservableList<Order> getOrderList() throws Exception {
        if (orderList.isEmpty()) {
            orderList.addAll(orderManager.getAllOrders());
        }

        return orderList;
    }

    public void takePictureClicked() throws Exception {
        if (currentOrder != null) {
            orderManager.openCamera(currentOrder);
        }
    }
}
