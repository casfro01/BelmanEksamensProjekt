package dk.eksamensprojekt.belmaneksamensprojekt.GUI.util;

/**
 * Enum som holder styr på hvilke vinduer som kan åbnes.
 */
public enum Windows {
    LoginWindow("LoginChooserWindow.fxml"),
    MainWindow("MainWindow.fxml"),
    OperatorWindow("OperatorWindow.fxml"),
    PhotoDocWindow("PhotodocumentWindow.fxml"),
    PreviewReportWindow("PreviewReportWindow.fxml"),
    PreviewPicturesWindow("PreviewPictures.fxml"),
    @Deprecated SmallOrBigWindow("SmallOrBigOrder.fxml"),
    LoginEmailWindow("LoginEmailWindow.fxml"),
    LoginChipWindow("LoginChipWindow.fxml");

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
