package dk.eksamensprojekt.belmaneksamensprojekt.BE;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id; // primærenøgle
    private Report report;
    private String orderNumber;
    private ObservableList<Image> images = FXCollections.observableArrayList();
    private Approved isApproved;
    private boolean documented;

    public Order(int id, String orderNumber, Report report, Approved isApproved) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.report = report;
        this.isApproved = isApproved;
        this.documented = false;
    }
    public Order(int id, String orderNumber, Report report, Approved isApproved, boolean documented) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.report = report;
        this.isApproved = isApproved;
        this.documented = documented;
    }

    public int getId() {
        return id;
    }

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
    public String getOrderNumber() {
        return orderNumber;
    }
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setImageList(ObservableList<Image> replicaImageList) {
        this.images = replicaImageList;
    }

    @Override
    public String toString(){
        return orderNumber;
    }
}