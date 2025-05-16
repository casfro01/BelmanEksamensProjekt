package dk.eksamensprojekt.belmaneksamensprojekt.BE;

public enum ImagePosition {
    EXTRA(0),
    FRONT(1),
    RIGHT(2),
    BEHIND(3),
    LEFT(4),
    TOP(5);

    private int id;

    ImagePosition(int id) {
        this.id = id;
    }

    public int toInt(){
        return id;
    }
}
