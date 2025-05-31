package dk.eksamensprojekt.belmaneksamensprojekt.BE;

/**
 * Base(simpel) ordre klasse, som kun har den essentielle informationer om en ordre, som id'et fra databasen (primærnøglen) -
 * og ordrenummeret.
 */
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
