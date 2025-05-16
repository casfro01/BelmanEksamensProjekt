package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Report;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Image;

import java.util.ArrayList;
import java.util.List;

public class OrderDaoFacade implements Repository<Order, String> {
    Repository<Order, String> ordersDAO = new OrdersDAO();
    ImageDAO imageDAO = new ImageDAO();
    Repository<Report, Integer> reportDAO = new ReportDAO();

    @Override
    public List<Order> getAll() throws Exception {
        List<Order> orders = ordersDAO.getAll();
        // TODO : kan det gøres bedre - lige nu har vi N + 1 kald af sql sætninger
        for (Order order : orders) {
            order.getImageList().addAll(imageDAO.getImageByOrder(order.getId()));
        }

        return orders;
    }

    @Override
    public Order getById(String orderNumber) throws Exception {
        // hent ordre
        Order returnOrder = ordersDAO.getById(orderNumber);

        // hent fulde report
        if (returnOrder.getReport() != null){
            returnOrder.setReport(reportDAO.getById(returnOrder.getReport().getId()));
        }

        // hent alle billeder
        returnOrder.getImageList().addAll(imageDAO.getImageByOrder(returnOrder.getId()));
        return returnOrder;
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

        // opdaterer imagelisten
        List<Image> tempList = new ArrayList<>();
        for(int i = 0; i < order.getImageList().size(); i++) {
            if (order.getImageList().get(i).getOrderID() > 0){
                tempList.add(order.getImageList().get(i));
            }
        }
        order.getImageList().setAll(tempList);
        // opdatér report
        if (order.getReport() != null && order.getReport().getId() <= 0 && order.getReport().getUser() != null) {
            Report report = reportDAO.create(order.getReport());
            order.setReport(report);
        }

        // opdatér seleve ordren:
        ordersDAO.update(order);
    }

    @Override
    public void delete(Order entity) throws Exception {
        ordersDAO.delete(entity);
    }
}
