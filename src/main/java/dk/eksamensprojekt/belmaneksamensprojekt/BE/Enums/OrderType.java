package dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums;

/**
 * Denne enum beskriver ordrens størrelse, hvorvidt det er en stor eller lille ordre... Måske mere i fremtiden, hvis nødvendigt.
 */
public enum OrderType {
    Small(5, 10),
    Large(5,100);

    // Min og max billeder som denne ordre skal kunne indeholde.
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

    /**
     * Denne metode returnerer en bestemt størrelse alt efter det id/index som bliver sat ind.
     * @param id 1 -> SMALL (lille ordre), 2 -> LARGE (stor ordre).
     */
    public static OrderType getTypeFromIndex(int id) {
        if (id == 2) {
            return OrderType.Large;
        }
        return OrderType.Small;
    }
}
