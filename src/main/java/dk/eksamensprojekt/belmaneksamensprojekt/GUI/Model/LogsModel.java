package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LogsModel {
    private OrderModel orderModel;
    private ObservableList<Order> cachedOrders;


    public LogsModel() {
        this.orderModel = new OrderModel();
        this.cachedOrders = FXCollections.observableArrayList();
    }

    //Henter alle order og cacher dem
    public ObservableList<Order> getAllOrders() throws Exception {
        cachedOrders.clear();
        cachedOrders.addAll(orderModel.getOrderList());
        return cachedOrders;
    }

    public Order getOrderByID(String id) throws Exception {
        return orderModel.searchOrder(id);
    }


    public ObservableList<Order> getCachedOrders() {
        return cachedOrders;
    }
}



