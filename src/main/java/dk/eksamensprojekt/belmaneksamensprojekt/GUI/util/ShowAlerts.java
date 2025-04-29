package dk.eksamensprojekt.belmaneksamensprojekt.GUI.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ShowAlerts {
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
}
