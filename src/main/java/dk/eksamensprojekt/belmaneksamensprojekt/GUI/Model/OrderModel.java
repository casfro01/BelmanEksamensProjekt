package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.*;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.Approved;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.ImagePosition;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.OrderManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;

// JavaFX
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Java
import java.util.ArrayList;
import java.util.List;

/**
 * Denne Model håndterer {@link Order}, og fungerer som mellemmand mellem BLL og GUI.
 * Denne model indeholder også en cached-liste med {@link Order}.
 */
public class OrderModel {
    private OrderManager orderManager;
    private ObservableList<Order> orderList;
    private Order currentOrder;

    public OrderModel() {
        this.orderManager = new OrderManager();
        this.orderList = FXCollections.observableArrayList();
    }

    /**
     * Hent {@link ObservableList<Order>} fra datakilden, hvis den cached-liste er tom.
     */
    public ObservableList<Order> getOrderList() throws Exception{
        if (orderList.isEmpty()) {
            orderList.addAll(orderManager.getAllOrders());
        }
        return orderList;
    }

    /**
     * Genlæs den cached-liste med ordre.
     */
    public ObservableList<Order> reloadOrderList() throws Exception {
        orderList.clear();
        orderList.addAll(orderManager.getAllOrders());
        return orderList;
    }

    /**
     * Når brugeren vil tage et billede:
     * @param imagePosition Den vinkel som billedet skal tages fra.
     * @return Det billede({@link Image}) som blev taget.
     */
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

    /**
     * Gem nuværende status
     */
    public void saveButtonClicked() throws Exception {
        orderManager.updateOrder(currentOrder);
    }

    /**
     * Send den nuværende ordre til godkendelse.
     */
    public void submitButtonClicked() throws Exception {
        orderManager.submitOrder(currentOrder);
    }

    /**
     * Sæt den valgt ordre
     * @param currentOrder Den valgte {@link Order}.
     */
    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    /**
     * @return Den nuværende {@link Order}.
     */
    public Order getCurrentOrder() {
        return this.currentOrder;
    }

    /**
     * Hent de ordre som skal godkendes
     * @return {@link List<Order>}
     */
    public List<Order> getOrdersForApproval() {
        List<Order> ordersForApproval = new ArrayList<>();
        // gennemløb alle ordre og find dem som skal godkendes.
        for (Order order : orderList) {
            if (order.isDocumented() && order.isApproved() == Approved.NOT_REVIEWED){
                ordersForApproval.add(order);
            }
        }
        return ordersForApproval;
    }

    /**
     * Opdater en {@link Order} i datakilden
     */
    public void updateOrder(Order order) throws Exception {
        orderManager.updateOrder(order);
    }

    /**
     * Hent en ordre baseret på ordrenummeret
     * @param txt Ordrenummeret
     * @return Den {@link Order} som matcher ordrenummeret der blev indsendt
     */
    public Order searchOrder(String txt) throws Exception {
        return orderManager.getById(txt);
    }
}
