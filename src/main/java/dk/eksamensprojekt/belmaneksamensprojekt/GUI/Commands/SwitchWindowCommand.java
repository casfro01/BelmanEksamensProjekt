package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Providers.ServiceProvider;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Services.WindowService;

/**
 * Denne kommando bruges til at skifte vinduer i programmet,
 * såsom at skifte til login-vinduet eller preview-report-vinduet.
 */
public class SwitchWindowCommand implements Command, UndoableCommand{
    private WindowService windowService;
    private final Windows WINDOW;

    /**
     * Constructor
     * @param window Det vindue som skal vises når kommandoen udføres.
     */
    public SwitchWindowCommand(Windows window) {
        this.WINDOW = window;
        // indsætter sig selv, for at få adgang til WindowServicen
        windowService = ServiceProvider.getServiceForCommand(this);
    }

    // udførsel
    @Override
    public void execute() {
        windowService.setPane(WINDOW);
    }

    // Undo kommandoen.
    // Denne del kunne nok laves på en smartere måde, men dette virker for nu, men er ikke venlig for skalering.
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
