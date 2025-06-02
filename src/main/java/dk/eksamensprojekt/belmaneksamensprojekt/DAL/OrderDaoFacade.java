package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Report;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Image;

// Java imports
import java.util.ArrayList;
import java.util.List;

/**
 * Denne klasse forbinder flere forskellige data-kilde-klasser, som gør det nemmere at hent/bygge ordrene, da der indgår -
 * meget data, da de forskellige attributter til ordren er delt ud på flere tabeller.
 * Disse tabeller har deres egen data-hentning-klasser, for at gøre det nemt at udskifte de forskellige komponenter
 */
public class OrderDaoFacade implements Repository<Order, String> {
    // Forskellige datakilder
    Repository<Order, String> ordersDAO = new OrdersDAO();
    ImageDAO imageDAO = new ImageDAO();
    Repository<Report, Integer> reportDAO = new ReportDAO();

    /**
     * Hent alle ordre fra databasen.
     */
    @Override
    public List<Order> getAll() throws Exception {
        // Først hentes alle ordre
        List<Order> orders = ordersDAO.getAll();
        // TODO : kan det gøres bedre - lige nu har vi N + 1 kald af sql sætninger
        // Derefter hentes alle billeder til hver ordre
        for (Order order : orders) {
            order.getImageList().addAll(imageDAO.getImageByOrder(order.getId()));
        }

        return orders;
    }

    /**
     * Hent en ordre baseret på dens ordrenummer.
     * @param orderNumber Den string som ordren defineres med.
     * @return Den ordre som muligvis bliver fundet.
     */
    @Override
    public Order getById(String orderNumber) throws Exception {
        // hent ordre
        Order returnOrder = ordersDAO.getById(orderNumber);

        // hent den fulde report
        if (returnOrder.getReport() != null){
            returnOrder.setReport(reportDAO.getById(returnOrder.getReport().getId()));
        }

        // hent alle billeder
        returnOrder.getImageList().addAll(imageDAO.getImageByOrder(returnOrder.getId()));
        return returnOrder;
    }

    @Override
    public Order create(Order order) throws Exception {
        // lav selve ordren
        Order newOrder = ordersDAO.create(order);
        // opdatere billederne op ordren
        imageDAO.updateAll(order.getImageList());
        // send tilbage
        return newOrder;
    }

    @Override
    public void update(Order order) throws Exception {
        // TODO : find en bedre løsning
        // opdatér billeder:
        List<Image> tempList = new ArrayList<>();
        // gennemgå listen -> fjern de billeder som ikke skal være der længere
        for (Image image : order.getImageList()) {
            if (image.getOrderID() > 0){
                // hvis billedet allerede er i databasen
                if (image.getId() > 0){
                    imageDAO.update(image);
                    tempList.add(image);
                }
                // hvis den ikke er i databasen
                else{
                    tempList.add(imageDAO.create(image));
                }
            }
            // hvis billedet ikke har et ordrenummer, så skal den slettes
            else{
                imageDAO.delete(image);
            }
        }

        // sæt den opdaterede liste
        order.setImageList(tempList);

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
