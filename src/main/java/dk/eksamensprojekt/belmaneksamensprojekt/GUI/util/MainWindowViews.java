package dk.eksamensprojekt.belmaneksamensprojekt.GUI.util;

public enum MainWindowViews {
    AllOrdersWindow("AllOrdersWindow.fxml"),
    OrderForApprovalWindow("OrderforApprovalWindow.fxml");



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
