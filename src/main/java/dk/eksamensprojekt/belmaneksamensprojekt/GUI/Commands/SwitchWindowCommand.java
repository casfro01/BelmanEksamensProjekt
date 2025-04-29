package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands;


import dk.eksamensprojekt.belmaneksamensprojekt.BE.Windows;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.WindowService;

public class SwitchWindowCommand implements Command, UndoableCommand{
    private final WindowService windowService;
    private Windows window;
    public SwitchWindowCommand(WindowService windowService) {
        this.windowService = windowService;
    }
    @Override
    public void execute() {
        windowService.setPane(window);
    }

    @Override
    public void undo() {

    }

    public void setWindow(Windows window){
        this.window = window;
    }
}
