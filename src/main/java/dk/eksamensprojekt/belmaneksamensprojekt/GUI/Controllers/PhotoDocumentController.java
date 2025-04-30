package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class PhotoDocumentController extends Controller implements Initializable {
    private ModelManager modelManager;
    private OrderModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modelManager = ModelManager.getInstance();
        model = modelManager.getOrderModel();
        //initializeComponents(1920, 1080);
    }

    @FXML
    private void takePictureClicked(ActionEvent event) throws Exception {
        model.takePictureClicked();
    }

    @FXML
    private void addPictureClicked(ActionEvent event) throws Exception {
        model.addPictureClicked();
    }

    @FXML
    private void saveButtonClicked(ActionEvent event) throws Exception {
        model.saveButtonClicked();
    }

    @FXML
    private void submitButtonClicked(ActionEvent event) throws Exception {
        model.submitButtonClicked();
    }
}
