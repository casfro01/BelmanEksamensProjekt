package dk.eksamensprojekt.belmaneksamensprojekt.BLL.util;

/**
 * Interface som sikre abstraktion, hvilket gør det nemt at udskifte den måde som der hashes.
 */
public interface IHashing {
    String hashString(String input) throws Exception;
    boolean compare(String input, String hash) throws Exception;
}