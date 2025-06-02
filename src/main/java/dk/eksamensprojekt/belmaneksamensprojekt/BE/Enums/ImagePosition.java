package dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums;

/**
 * Enum som bruges til at beskrive hvilken position et billede har på et produkt.
 * Hvorvidt billedet er tage forfra, bagved, på en af siderne etc.
 */
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

    /**
     * @return Integer værdien som billede-positionen svarer til.
     */
    public int toInt(){
        return id;
    }

    /**
     * Oversæt en integer til et position.
     * @param id Det position-id som positionen svarer til.
     * @return En position-enum-værdi baseret på det indsatte id.
     */
    public static ImagePosition fromInt(int id){
        // Hvis id'et er udenfor rækkevidden - så skal den returnere default
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
