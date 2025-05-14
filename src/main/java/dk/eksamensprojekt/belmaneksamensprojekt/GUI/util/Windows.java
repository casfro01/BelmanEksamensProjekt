package dk.eksamensprojekt.belmaneksamensprojekt.GUI.util;

public enum Windows {
    LoginWindow("LoginChooserWindow.fxml"),
    MainWindow("MainWindow.fxml"),
    OperatorWindow("OperatorWindow.fxml"),
    PhotoDocWindow("PhotodocumentWindow.fxml"),
    PreviewReportWindow("PreviewReportWindow.fxml"),
    UserWindow("UserWindow.fxml"),
    PreviewPicturesWindow("PreviewPictures.fxml"),
    LoginEmailWindow("LoginEmailWindow.fxml"),
    LoginChipWindow("LoginChipWindow.fxml"),;

    private final String path;

    Windows(String path){
        this.path = path;
    }

    public String getPath(){
        return path;
    }

    public String getName(){
        return name();
    }
}
