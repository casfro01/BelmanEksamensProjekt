package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Approved;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.OrderManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class OrderModel {
    private OrderManager orderManager;
    private ObservableList<Order> orderList;
    private Order currentOrder;

    public OrderModel() {
        this.orderManager = new OrderManager();
        this.orderList = FXCollections.observableArrayList();
    }

    public ObservableList<Order> getOrderList() {
        /*
        if (orderList.isEmpty()) {
            orderList.addAll(orderManager.getAllOrders());
        }

         */
        return orderList;
    }
    public ObservableList<Order> reloadOrderList() throws Exception {
        orderList.clear();
        orderList.addAll(orderManager.getAllOrders());
        return orderList;
    }

    public void takePictureClicked() throws Exception {
        if (currentOrder != null) {
            orderManager.openCamera(currentOrder, ModelManager.getInstance().getUserModel().getSelectedUser().get());
        }
    }

    public void addPictureClicked() throws Exception {
        orderManager.addPicFromFolder(currentOrder, ModelManager.getInstance().getUserModel().getSelectedUser().get());
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
            if (order.isDocumented() && order.isApproved() == Approved.NotReviewed){
                ordersForApproval.add(order);
            }
        }
        return ordersForApproval;
    }

    public void updateOrder(Order order) throws Exception {
        orderManager.updateOrder(order);
    }

    public Order searchOrder(String txt) throws Exception {
        return orderManager.getById(txt);
    }
}
