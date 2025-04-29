package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PhotoDocumentController extends Controller implements Initializable {
    private ModelManager modelManager;
    private OrderModel model;
    @Override
    public void initializeComponents(double width, double height) {
        modelManager = ModelManager.getInstance();
        model = modelManager.getOrderModel();
        // KALD SUPER INITIALIZECOMPONENTS HER?? ANCHOR PANE???
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeComponents(1920, 1080);
    }

    @FXML
    private void takePictureClicked(ActionEvent event) throws Exception {
        model.takePictureClicked();
    }

}
