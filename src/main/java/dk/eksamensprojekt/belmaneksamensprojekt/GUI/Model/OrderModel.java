package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.OrderManager;
import javafx.collections.ObservableList;

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

    public void addPictureClicked() throws Exception {
        orderManager.addPicFromFolder(currentOrder);
    }

    public void saveButtonClicked() throws Exception {
        orderManager.updateOrder(currentOrder);
    }

    public void submitButtonClicked() throws Exception {
        orderManager.submitOrder(currentOrder);
    }
    public void updateOrder() throws Exception {
        orderManager.updateOrder(currentOrder);
    }

}
