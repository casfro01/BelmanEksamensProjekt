package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Services.MainWindowService;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.MainWindowViews;
import dk.eksamensprojekt.belmaneksamensprojekt.Main;

public class SwitchMainView implements Command{
    private final MainWindowViews mainWindowViews;
    private final MainWindowService mainWindowService;
    public SwitchMainView(MainWindowService mainWindowService, MainWindowViews mainWindowViews) {
        this.mainWindowViews = mainWindowViews;
        this.mainWindowService = mainWindowService;
    }

    @Override
    public void execute() {
        mainWindowService.setView(mainWindowViews);
    }
}
