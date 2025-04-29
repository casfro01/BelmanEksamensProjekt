package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands;


import dk.eksamensprojekt.belmaneksamensprojekt.BE.Windows;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.WindowService;

public class SwitchWindowCommand implements Command, UndoableCommand{
    private WindowService windowService;
    private Windows window;
    public SwitchWindowCommand(WindowService windowService) {

    }
    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }

    public void setWindow(Windows window){
        this.window = window;
    }
}
