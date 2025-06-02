package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands;

/**
 * Undo kommandoen, hvis kommandoen implementerer dette interface, så betyder det at den kan fortrydes.
 */
public interface UndoableCommand {
    /**
     * Når denne metode kører, så fortrydes kommandoen.
     */
    void undo();
}
