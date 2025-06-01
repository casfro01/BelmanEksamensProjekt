package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Providers;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.Command;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Services.WindowService;

/**
 * Denne klasse har til formål at stille services til rådighed (kan erstattes af DI)
 */
public class ServiceProvider {
    // Hvis dette skal opskalere, så ændre det til et map, så man er fri for at lave så mange IFs
    private static WindowService windowService;

    /**
     * Hent den service som kommandoerne benytter til at udføre deres opgave.
     * (Denne metode indkapsler data -> så hvis man ikke er en kommando, så kan man ikke få fat i servicen).
     * @param cmd Den {@link Command} som kræver servicen.
     * @return {@link WindowService}
     */
    public static WindowService getServiceForCommand(Command cmd) {
        if (cmd instanceof SwitchWindowCommand){
            return windowService;
        }

        return null;
    }

    /**
     * Sæt den aktive {@link WindowService}.
     */
    public static void setWindowService(WindowService windowService) {
        ServiceProvider.windowService = windowService;
    }
}
