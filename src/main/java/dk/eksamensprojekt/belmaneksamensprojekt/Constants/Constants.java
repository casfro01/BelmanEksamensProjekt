package dk.eksamensprojekt.belmaneksamensprojekt.Constants;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;

import java.io.File;

public class Constants {
    public static final String IMAGES_PATH = System.getProperty("user.dir") + File.separator + "Images" + File.separator;
    public static final String REPORTS_PATH = System.getProperty("user.dir") + File.separator + "Reports" + File.separator;

    public static double DISPLAY_TIME = 1.5;


    public static void DisplayError(String title, String message) {
        ShowAlerts.displayWarning(title, message);
    }
}
