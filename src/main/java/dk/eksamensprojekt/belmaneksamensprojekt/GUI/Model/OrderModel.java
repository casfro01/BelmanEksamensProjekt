package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.*;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.OrderManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class OrderModel {
    private OrderManager orderManager;
    private ObservableList<Order> orderList;
    private Order currentOrder;
    private OrderType currentOrderType;

    public OrderModel() {
        this.orderManager = new OrderManager();
        this.orderList = FXCollections.observableArrayList();
    }

    public ObservableList<Order> getOrderList() throws Exception{
        if (orderList.isEmpty()) {
            orderList.addAll(orderManager.getAllOrders());
        }
        return orderList;
    }
    public ObservableList<Order> reloadOrderList() throws Exception {
        orderList.clear();
        orderList.addAll(orderManager.getAllOrders());
        return orderList;
    }

    public Image takePictureClicked(ImagePosition imagePosition) throws Exception {
        if (currentOrder != null) {
            try {
                return orderManager.openCamera(
                        currentOrder,
                        ModelManager.INSTANCE.getUserModel().getSelectedUser().get(),
                        imagePosition
                );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void saveButtonClicked() throws Exception {
        orderManager.updateOrder(currentOrder);
    }

    public void submitButtonClicked() throws Exception {
        orderManager.submitOrder(currentOrder);
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public Order getCurrentOrder() {
        return this.currentOrder;
    }

    public List<Order> getOrdersForApproval() {
        List<Order> ordersForApproval = new ArrayList<>();
        for (Order order : orderList) {
            if (order.isDocumented() && order.isApproved() == Approved.NOT_REVIEWED){
                ordersForApproval.add(order);
            }
        }
        return ordersForApproval;
    }

    public OrderType getCurrentOrderType() {
        return this.currentOrderType;
    }

    public void updateOrder(Order order) throws Exception {
        orderManager.updateOrder(order);
    }

    public Order searchOrder(String txt) throws Exception {
        return orderManager.getById(txt);
    }

    public void setCurrentOrderType(OrderType type) {
        this.currentOrderType = type;
    }

}
