package dk.eksamensprojekt.belmaneksamensprojekt.BE;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class Order extends BaseOrder{
    public static String DATE_FORMAT = "dd-MM-yyyy";

   // private int id; // primærenøgle
    private Report report;
    //private String orderNumber;
    private ObservableList<Image> images = FXCollections.observableArrayList();
    private Approved isApproved;
    private boolean documented;
    private OrderType orderType;
    private LocalDate orderDate = LocalDate.now();

    public Order(int id, String orderNumber, Report report, Approved isApproved) {
        super(id, orderNumber);
        //this.id = id;
        //this.orderNumber = orderNumber;
        this.report = report;
        this.isApproved = isApproved;
        this.documented = false;
        this.orderType = OrderType.Small;
    }
    public Order(int id, String orderNumber, Report report, Approved isApproved, boolean documented) {
        super(id, orderNumber);
        //this.id = id;
        //this.orderNumber = orderNumber;
        this.report = report;
        this.isApproved = isApproved;
        this.documented = documented;
        this.orderType = OrderType.Small;
    }

    /*
    public int getId() {
        return id;
    }

     */

    public Report getReport() {
        return report;
    }

    public ObservableList<Image> getImageList() {
        return images;
    }

    public void setApproved(Approved isApproved) {
        this.isApproved = isApproved;
    }

    public Approved isApproved() {
        return isApproved;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public boolean isDocumented() {
        return documented;
    }

    public void setDocumented(boolean documented) {
        this.documented = documented;
    }
    /*
    public String getOrderNumber() {
        return orderNumber;
    }
     */
    /*
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
     */

    public void setImageList(ObservableList<Image> replicaImageList) {
        this.images = replicaImageList;
    }

    public OrderType getType() {
        return orderType;
    }
    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public LocalDate getOrderDate(){
        return orderDate;
    }
    public void setOrderDate(LocalDate date){
        orderDate = date;
    }
}