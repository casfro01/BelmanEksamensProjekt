package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import java.util.List;

/**
 * Interface til hvordan en DAO klasse kan opbygges. Hvis klassen følger dette interface vil det være nemt at udskifte
 * klassen, da klasserne følger denne opbygning.
 * @param <T> Typen af objekt/klasse som hentes fra databasen - brugere, ordre, rapport etc.
 * @param <ID> Den id-type som gør objektet/klassen unikt -> int, string short etc.
 */
public interface Repository<T, ID>{
    //
    // Hent data
    //
    /**
     * Hent alle {@link T}
     * @return Liste med {@link T}
     */
    List<T> getAll() throws Exception;

    /**
     * Hent {@link T} baseret på {@link ID}
     * @param id Det {@link ID} som definerer/matcher objektet
     * @return {@link T}
     */
    T getById(ID id) throws Exception;

    //
    // Modificer data
    //
    /**
     * Lav en ny {@link T}
     * @param entity Den {@link T} som skal laves.
     * @return En ny {@link T} med nyt id - givet af datakilden
     */
    T create(T entity) throws Exception;

    /**
     * Opdatér {@link T} med nye informationer.
     * @param entity Den {@link T} som skal ændres
     */
    void update(T entity) throws Exception;

    /**
     * Slet en {@link T} fra datakilden
     * @param entity Den {@link T} som skal slettes
     */
    void delete(T entity) throws Exception;
}
