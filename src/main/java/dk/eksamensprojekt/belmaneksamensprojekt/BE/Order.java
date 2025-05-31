package dk.eksamensprojekt.belmaneksamensprojekt.BE;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.Approved;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.OrderType;

// Java imports
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Denne ordre-klasse er en forlængelse af base-ordren, hvor base-klassen udviddes med flere informationer, såsom
 * - Rapport,
 * - Billeder,
 * - Dato,
 * - osv...
 */
public class Order extends BaseOrder{
    //
    // Statiske konstanter
    //
    // formatering af dato
    public static String DATE_FORMAT = "dd-MM-yyyy";

    //
    // Instans-variabler
    //
    private Report report;
    private List<Image> images = new ArrayList<>();
    private Approved isApproved;
    private boolean documented;
    private OrderType orderType;
    private LocalDate orderDate = LocalDate.now();

    //
    // Constructors
    //
    public Order(int id, String orderNumber, Report report, Approved isApproved) {
        super(id, orderNumber);
        this.report = report;
        this.isApproved = isApproved;
        this.documented = false;
        this.orderType = OrderType.Small;
    }
    public Order(int id, String orderNumber, Report report, Approved isApproved, boolean documented) {
        super(id, orderNumber);
        this.report = report;
        this.isApproved = isApproved;
        this.documented = documented;
        this.orderType = OrderType.Small;
    }

    //
    // Getters
    //
    public Report getReport() {
        return report;
    }

    public List<Image> getImageList() {
        return images;
    }

    public Approved isApproved() {
        return isApproved;
    }

    public boolean isDocumented() {
        return documented;
    }

    public OrderType getType() {
        return orderType;
    }

    public LocalDate getOrderDate(){
        return orderDate;
    }

    //
    // Setters
    //
    public void setApproved(Approved isApproved) {
        this.isApproved = isApproved;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public void setDocumented(boolean documented) {
        this.documented = documented;
    }

    public void setImageList(List<Image> replicaImageList) {
        this.images = replicaImageList;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public void setOrderDate(LocalDate date){
        orderDate = date;
    }
}