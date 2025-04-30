package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Image;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;

import java.util.List;

public class OrderDaoFacade implements Repository<Order, Integer> {
    Repository<Order, Integer> ordersDAO = new OrdersDAO();
    UpdateAll<Image> imageDAO = new ImageDAO(); // der bruges en specifik metode fra denne som ikke er med i repo

    @Override
    public List<Order> getAll() throws Exception {
        return ordersDAO.getAll();
    }

    @Override
    public Order getById(Integer integer) throws Exception {
        return ordersDAO.getById(integer);
    }

    @Override
    public Order create(Order order) throws Exception {
        imageDAO.updateAll(order.getImageList());
        return ordersDAO.create(order);
    }

    @Override
    public void update(Order order) throws Exception {
        // opdatér billeder:
        imageDAO.updateAll(order.getImageList());

        // opdatér seleve ordren:
        ordersDAO.update(order);

    }

    @Override
    public void delete(Order entity) throws Exception {
        ordersDAO.delete(entity);
    }
}
