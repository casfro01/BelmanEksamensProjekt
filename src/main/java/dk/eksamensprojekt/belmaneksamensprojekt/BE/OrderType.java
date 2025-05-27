package dk.eksamensprojekt.belmaneksamensprojekt.BE;

public enum OrderType {
    Small(5, 10),
    Large(5,100);

    private final int MIN;
    private final int MAX;

    OrderType(int min, int max) {
        MIN = min;
        MAX = max;
    }

    public int getMin() {
        return MIN;
    }
    public int getMax() {
        return MAX;
    }

    public static OrderType getTypeFromIndex(int id) {
        if (id == 2) {
            return OrderType.Large;
        }
        return OrderType.Small;
    }
}
