package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import java.util.List;

/**
 * Dette interface tillader at man kan opdatere flere typer på én gang.
 * @param <T> Den datatype / klasse-type som klassen skal håndtere.
 */
public interface UpdateAll<T>{
    /**
     * Opdatér en liste med {@link T}
     */
    void updateAll(List<T> items) throws Exception;
}
