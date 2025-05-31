package dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums;

/**
 * Fortæller hvorvidt objektet, den er forbundet til, er godkendt eller afvist.
 * Oversætter et id til en værdi.
 * (Man brude nok refactor NOT_APPROVED til rejected)
 */
public enum Approved {
    APPROVED(1),
    NOT_APPROVED(0),
    NOT_REVIEWED(2);

    private int id;

    Approved(int id){
        this.id = id;
    }

    /**
     * Oversæt værdien til en boolean, som kan bruges til at fortælle om objektet er godkendt eller ej.
     * @return Returnerer en boolean baseret på om objektet er godkendt eller ej
     */
    public boolean toBoolean(){
        return id == 1;
    }

    /**
     * Oversætter en boolean værdi til en enum-værdi
     * @param value -> true eller false -> Approved / not approved
     * @return Returnerer en enum-værdi tilsvarende til en boolean værdi
     */
    public static Approved valueOfBoolean(boolean value){
        return value ? APPROVED : NOT_APPROVED;
    }

    @Override
    public String toString() {
        switch (id){
            case 1 -> {
                return "Approved";
            }
            case 0 -> {
                return "Not approved";
            }
            default -> {
                return "Not reviewed";
            }
        }
    }
}
