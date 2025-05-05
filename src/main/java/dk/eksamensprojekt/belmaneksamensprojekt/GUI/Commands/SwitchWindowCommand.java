package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands;


import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.WindowService;

public class SwitchWindowCommand implements Command, UndoableCommand{
    private WindowService windowService;
    private Windows window;

    public SwitchWindowCommand(Windows window) {
        this.window = window;
    }
    @Override
    public void execute() {
        windowService.setPane(window);
    }

    @Override
    public void undo() {

    }

    public void setWindowService(WindowService windowService) {
        this.windowService = windowService;
    }

    public void setWindow(Windows window){
        this.window = window;
    }
}
