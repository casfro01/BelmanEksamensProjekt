package dk.eksamensprojekt.belmaneksamensprojekt.BE;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id; // primærenøgle
    private Report report;
    private String orderNumber;
    private Approved approved;
    private List<Image> images = new ArrayList<>();
    private Approved isApproved;

    public Order(int id, String orderNumber, Report report, Approved isApproved) {
        this.id = id;
        this.orderNumber = orderNumber; // setter til denne ? TODO : brug regex ?
        this.report = report;
        this.isApproved = isApproved;
    }

    public int getId() {
        return id;
    }

    public Report getReport() {
        return report;
    }

    public List<Image> getImageList() {
        return images;
    }

    public void setApproved(Approved isApproved) {
        this.isApproved = isApproved;
    }

    public Approved isApproved() {
        return approved;
    }
}