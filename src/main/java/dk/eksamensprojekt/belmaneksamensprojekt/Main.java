package dk.eksamensprojekt.belmaneksamensprojekt;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Providers.InvokerProvider;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Providers.ServiceProvider;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Services.WindowService;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage){
        // sæt service for vinduer
        ServiceProvider.setWindowService(new WindowService(stage));

        // eksekverer kommando
        SwitchWindowCommand command = new SwitchWindowCommand(Windows.LoginWindow);
        InvokerProvider.getInvoker().executeCommand(command);
    }

    public static void main(String[] args) {
        launch();
    }
}
