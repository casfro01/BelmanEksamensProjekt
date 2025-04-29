package dk.eksamensprojekt.belmaneksamensprojekt.GUI;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.Command;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.UndoableCommand;

import java.util.Stack;

public class WindowInvoker {
    // command history
    // TODO : implementér undo bagefter
    private Stack<UndoableCommand> commandHistory = new Stack<>();

    public void executeCommand(Command command){
        command.execute();
        if (command instanceof UndoableCommand uCommand){
            commandHistory.push(uCommand);
        }
    }

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
