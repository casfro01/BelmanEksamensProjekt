package dk.eksamensprojekt.belmaneksamensprojekt.BLL.util;

public interface IHashing {
    // tilføj metoden ;)
    String hashString(String input) throws Exception;
    boolean compare(String input, String hash) throws Exception;
}