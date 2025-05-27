package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands;


import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Providers.ServiceProvider;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Services.WindowService;

public class SwitchWindowCommand implements Command, UndoableCommand{
    private WindowService windowService;
    private final Windows WINDOW;

    public SwitchWindowCommand(Windows window) {
        this.WINDOW = window;
        // indsætter sig selv, for at få adgang til WindowServicen
        windowService = ServiceProvider.getServiceForCommand(this);
    }
    @Override
    public void execute() {
        windowService.setPane(WINDOW);
    }

    @Override
    public void undo() {
        switch (WINDOW){
            case PhotoDocWindow -> windowService.setPane(Windows.OperatorWindow);
            case PreviewPicturesWindow -> windowService.setPane(Windows.MainWindow);
            case PreviewReportWindow -> windowService.setPane(Windows.PreviewPicturesWindow);
            default -> windowService.setPane(Windows.LoginWindow);
        }

    }
}
