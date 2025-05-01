package dk.eksamensprojekt.belmaneksamensprojekt;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.WindowInvoker;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.WindowService;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage){
        /* WindowService windowService = new WindowService(stage);
        WindowInvoker windowInvoker = new WindowInvoker();
        SwitchWindowCommand command = new SwitchWindowCommand(windowService);
        command.setWindow(Windows.PhotoDocWindow);
        windowInvoker.executeCommand(command);*/
    }

    public static void main(String[] args) {
        launch();
    }
}
