package dk.eksamensprojekt.belmaneksamensprojekt.BE;

public class BaseOrder {
    private final int id;
    private final String orderNumber;

    public BaseOrder(int id, String orderNumber) {
        this.id = id;
        this.orderNumber = orderNumber;
    }

    public int getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    @Override
    public String toString(){
        return orderNumber;
    }
}
