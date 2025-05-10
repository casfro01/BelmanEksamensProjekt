package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Providers;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.Command;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.WindowService;

public class ServiceProvider {
    // Hvis dette skal opskalere, så ændre det til et map, så man er fri for at lave så mange ifs
    private static WindowService windowService;
    public static WindowService getServiceForCommand(Command cmd) {
        if (cmd instanceof SwitchWindowCommand){
            return windowService;
        }

        return null;
    }

    public static void setWindowService(WindowService windowService) {
        ServiceProvider.windowService = windowService;
    }
}
