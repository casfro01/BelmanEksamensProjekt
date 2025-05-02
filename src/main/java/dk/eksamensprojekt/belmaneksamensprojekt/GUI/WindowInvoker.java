package dk.eksamensprojekt.belmaneksamensprojekt.GUI;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.Command;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.UndoableCommand;

import java.util.Stack;

public class WindowInvoker {
    private final WindowService windowService;
    // command history
    // TODO : implementér undo bagefter
    private Stack<UndoableCommand> commandHistory = new Stack<>();
    public WindowInvoker(WindowService windowService) {
        this.windowService = windowService;
    }

    public void executeCommand(Command command){
        // set service
        // jeg er usikker på om dette er acceptabelt, men det giver mere mening ift. før, hvor en ny service skulle laves
        // man kunne også lave en get instance ting på servicen, men det giver ikke så meget mening ift. dette tilfælde
        if (command instanceof SwitchWindowCommand swc) {
            swc.setWindowService(windowService);
        }
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
