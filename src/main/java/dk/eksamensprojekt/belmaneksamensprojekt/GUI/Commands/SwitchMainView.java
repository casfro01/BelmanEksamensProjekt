package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Services.MainWindowService;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.MainWindowViews;

/**
 * Skifte view i hovedvinduet -> bruges til at vise forskellige elementer, såsom alle ordre tabellen, eller hvilke
 * ordre som skal godkendes.
 */
public class SwitchMainView implements Command{
    private final MainWindowViews mainWindowViews;
    private final MainWindowService mainWindowService;

    /**
     * @param mainWindowService Den service som har ansvar/holder styr på hvad der bliver vist på hovedvinduet
     * @param mainWindowViews Det view som skal vises
     */
    public SwitchMainView(MainWindowService mainWindowService, MainWindowViews mainWindowViews) {
        this.mainWindowViews = mainWindowViews;
        this.mainWindowService = mainWindowService;
    }

    @Override
    public void execute() {
        mainWindowService.setView(mainWindowViews);
    }
}
