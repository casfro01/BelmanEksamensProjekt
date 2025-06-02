package dk.eksamensprojekt.belmaneksamensprojekt.GUI;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.Command;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.UndoableCommand;

// Java imports
import java.util.Stack;

/**
 * Denne klasse har til formål at gemme, eksekvere og fortryde kommandoer.
 */
public class WindowInvoker {
    // command history
    private Stack<UndoableCommand> commandHistory = new Stack<>();

    /**
     * Eksekvér en kommando.
     * @param command Den {@link Command} som skal eksekveres
     */
    public void executeCommand(Command command){
        // execute
        command.execute();
        // gemme -> hvis den kan fortrydes.
        if (command instanceof UndoableCommand uCommand){
            commandHistory.push(uCommand);
        }
    }

    /**
     * Fortryd den forrige eksekverede kommando.
     */
    public void undoLastCommand(){
        if (!commandHistory.isEmpty()){
            UndoableCommand command = commandHistory.pop();
            command.undo();
        }
        else{
            System.out.println("No commands available!");
        }
    }

}
