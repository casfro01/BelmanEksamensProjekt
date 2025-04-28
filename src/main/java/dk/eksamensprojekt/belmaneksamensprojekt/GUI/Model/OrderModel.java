package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.OrderManager;
import javafx.collections.ObservableList;

public class OrderModel {
    private OrderManager orderManager;
    private ObservableList<Order> orderList;

    public OrderModel() {
        this.orderManager = new OrderManager();
    }

    public ObservableList<Order> getOrderList() throws Exception {
        if (orderList.isEmpty()) {
            orderList.addAll(orderManager.getAllOrders());
        }

        return orderList;
    }
}
