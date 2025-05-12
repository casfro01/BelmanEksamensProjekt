package dk.eksamensprojekt.belmaneksamensprojekt.GUI;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.Command;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.UndoableCommand;

import java.util.Stack;

public class WindowInvoker {
    // command history
    private Stack<UndoableCommand> commandHistory = new Stack<>();
    public WindowInvoker() {
    }

    public void executeCommand(Command command){
        // execute
        command.execute();
        // gemme
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
