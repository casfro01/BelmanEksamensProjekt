package dk.eksamensprojekt.belmaneksamensprojekt.BLL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.OrdersDAO;

import java.util.List;

public class OrderManager {
    private final OrdersDAO ordersDAO;

    public OrderManager() {
        this.ordersDAO = new OrdersDAO();
    }
    public List<Order> getAllOrders() throws Exception {
        return ordersDAO.getAll();
    }
}
