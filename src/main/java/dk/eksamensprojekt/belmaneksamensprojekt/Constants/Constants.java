package dk.eksamensprojekt.belmaneksamensprojekt.Constants;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;

import java.io.File;

public class Constants {
    public static final String IMAGES_PATH = System.getProperty("user.dir") + File.separator + "Images" + File.separator;
    public static double DISPLAY_TIME = 1.5;
    public static final int SMALL_ORDER_MAX = 10;
    public static final int SMALL_ORDER_MIN = 5;
    public static final int LARGE_ORDER_MAX = 100;
    public static final int LARGE_ORDER_MIN = 5;


    public static void DisplayError(String title, String message) {
        ShowAlerts.splashMessage(title, message, DISPLAY_TIME);
    }
}
