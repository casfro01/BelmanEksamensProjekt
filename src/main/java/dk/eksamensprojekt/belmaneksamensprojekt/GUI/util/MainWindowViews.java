package dk.eksamensprojekt.belmaneksamensprojekt.GUI.util;

/**
 * Enum som holder styr på hvilke vinduer der kan skiftes til på main-vinduet.
 */
public enum MainWindowViews {
    AllOrdersWindow("AllOrdersWindow.fxml"),
    OrderForApprovalWindow("OrderforApprovalWindow.fxml"),
    UserWindow("UserWindow.fxml"),
    AdminLogWindow("AdminLogWindow.fxml");



    private final String path;

    MainWindowViews(String path){
        this.path = path;
    }

    public String getPath(){
        return path;
    }

    public String getName(){
        return name();
    }
}
