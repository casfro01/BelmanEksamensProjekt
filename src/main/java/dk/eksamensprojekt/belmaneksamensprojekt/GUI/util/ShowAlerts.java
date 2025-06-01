package dk.eksamensprojekt.belmaneksamensprojekt.GUI.util;

import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;

public class ShowAlerts {
    public static double DEFAULT_DISPLAY_TIME = 1.5;
    /**
     * Dette vindue lader brugeren tage en beslutning - om brugeren trykker på knappen ok - eller ej
     * @param title Hvad skal titlen af advarslen være
     * @param warningMessage Hvad skal advarslen sige
     * @return Returnerer true hvis brugeren trykker på ok - alt andet returnerer false
     */
    public static boolean displayWarning(String title, String warningMessage){
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle(title);
        confirmation.setContentText(warningMessage);
        var result = confirmation.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Display en fejlbesked for brugeren
     * @param title Titlen på vinduet
     * @param message Hovedfejlen
     */
    public static void displayError(String title, String message){
        displayMessage(title, message, Alert.AlertType.ERROR);
    }

    /**
     * Display en besked for brugeren
     * @param title Titlen på vinduet
     * @param message Hovedbeskeden
     * @param alertType Hvilken type alert det skal være -> WARNING, INFORMATION, ERROR etc.
     */
    public static void displayMessage(String title, String message, Alert.AlertType alertType){
        Alert messageAlert = new Alert(alertType);
        messageAlert.setTitle(title);
        messageAlert.setContentText(message);
        messageAlert.showAndWait();
    }

    /**
     * Display en besked for brugeren i et bestemt antal sekunder, med mindre de gør noget
     * Informationstypen er standard typen, hvis ikke andet er valgt
     * @param title titlen på beskeden
     * @param message 'kroppen' af beskeden - hvad skal den sige
     * @param time hvor lang tid skal den vises i sekunder
     */
    public static void splashMessage(String title, String message, double time){
        splashMessage(title, message, time, Alert.AlertType.INFORMATION);
    }

    // TODO : måske laves til sit egent pane? -> for man kan ikke fjerne buttons - da vinduet så ikke vil lukke
    public static void splashMessage(String title, String message, double time, Alert.AlertType alertType){
        Alert messageAlert = new Alert(alertType);
        messageAlert.setTitle(title);
        messageAlert.setContentText(message);
        messageAlert.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(time));
        delay.setOnFinished(event -> messageAlert.close());
        delay.play();
    }
}
