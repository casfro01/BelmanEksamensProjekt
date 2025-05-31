package dk.eksamensprojekt.belmaneksamensprojekt.BE;

/**
 * Base(simpel) user(bruger) klasse, som kun har den essentielle informationer om en bruger, som id'et fra databasen (primærnøglen) -
 * og brugerens fulde navn (name).
 */
public class BaseUser {
    private int id;
    private String name;

    public BaseUser(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
