package dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums;

public enum Approved {
    APPROVED(1),
    NOT_APPROVED(0),
    NOT_REVIEWED(2);

    private int id;

    Approved(int id){
        this.id = id;
    }

    public boolean toBoolean(){
        return id == 1;
    }

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
