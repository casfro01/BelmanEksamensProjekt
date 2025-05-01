package dk.eksamensprojekt.belmaneksamensprojekt;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.WindowInvoker;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.WindowService;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;

public class Main extends Application {
    @Override
    public void start(Stage stage){
        WindowService windowService = new WindowService(stage);
        WindowInvoker in = new WindowInvoker();
        SwitchWindowCommand switchWindowCommand = new SwitchWindowCommand(windowService);
        //switchWindowCommand.setWindow(Windows.PhotoDocWindow);
        //in.executeCommand(switchWindowCommand);
    }

    public static void main(String[] args) {
        launch();
    }
}
