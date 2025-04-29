package dk.eksamensprojekt.belmaneksamensprojekt.BE;

public enum Windows {
    LoginWindow("LoginWindow.fxml"),
    MainWindow("MainWindow.fxml"),
    OperatorWindow("OperatorWindow.fxml"),
    PhotoDocWindow("PhotodocumentWindow.fxml");

    private final String path;

    Windows(String path){
        this.path = path;
    }

    public String getPath(){
        return path;
    }
}
