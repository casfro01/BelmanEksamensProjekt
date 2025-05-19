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

    public static ImagePosition fromInt(int id){
        // hvis id'et er udenfor rækkevidden - så skal den returnere default
        ImagePosition[] positions = ImagePosition.values();
        if (id < 0 || id >= positions.length)
            return ImagePosition.EXTRA;

        // Dette virker fordi vi har valgt at sætte dem i rækkefølge.
        return positions[id];
    }

    @Override
    public String toString() {
        String name = name().toLowerCase();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }
}
