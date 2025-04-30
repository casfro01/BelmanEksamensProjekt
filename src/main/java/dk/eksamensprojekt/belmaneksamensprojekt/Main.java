package dk.eksamensprojekt.belmaneksamensprojekt;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.WindowService;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage){
        WindowService windowService = new WindowService(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
