package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Log;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.LogModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class LogController extends Controller implements Initializable {

    private LogModel logModel;

    @FXML
    private TableView<Log> logTableView;

    @FXML
    private AnchorPane paneOrder;

    //Ordrenummer
    @FXML
    private TableColumn<Log, String> tblColOrderNumberAdmin;

    //Navn på brugeren
    @FXML
    private TableColumn<Log, String> tblColName;

    //handlingen
    @FXML
    private TableColumn<Log, String> tblColSignature;

    //Dato
    @FXML
    private TableColumn<Log, LocalDate> tblColDate;
    @FXML
    private TextField txtOrderSearchbarAdmin;
    @FXML
    private ImageView imgSearchAdmin;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logModel = ModelManager.INSTANCE.getLogModel();
        initializeTableView();

    }

    private void initializeTableView() {
        tblColOrderNumberAdmin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
        tblColName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
        tblColSignature.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
        tblColDate.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        try {
            logTableView.setItems(logModel.getAllLogs());
        } catch (Exception e) {
            ShowAlerts.displayMessage("Problem", "Could not load logs", Alert.AlertType.ERROR);
        }



    }
    private boolean searchInLogs(Log log) {
        return false;
    }
}
