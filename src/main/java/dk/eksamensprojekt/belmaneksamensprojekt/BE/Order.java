package dk.eksamensprojekt.belmaneksamensprojekt.BE;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int id; // primærenøgle
    private Report report;
    private String orderNumber;
    private List<Image> images = new ArrayList<>();

    public Order(int id, String orderNumber, Report report) {
        this.id = id;
        this.orderNumber = orderNumber; // setter til denne ? TODO : brug regex ?
        this.report = report;
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
}