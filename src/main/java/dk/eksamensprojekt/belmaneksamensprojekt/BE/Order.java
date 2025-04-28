package dk.eksamensprojekt.belmaneksamensprojekt.BE;

public class Order {
    private int id; // primærenøgle
    private Report report;

    public Order(int id, Report report) {
        this.id = id;
        this.report = report;
    }

    public int getId() {
        return id;
    }

    public Report getReport() {
        return report;
    }
}
