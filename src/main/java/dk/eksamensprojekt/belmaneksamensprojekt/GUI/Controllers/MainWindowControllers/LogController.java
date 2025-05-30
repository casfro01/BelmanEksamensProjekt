package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers.MainWindowControllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.BaseOrder;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.BaseUser;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Log;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.LogModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.BackgroundTask;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts.displayError;

public class LogController extends Controller implements Initializable {

    private LogModel logModel;

    @FXML
    private TableView<Log> logTableView;

    @FXML
    private AnchorPane paneOrder;

    //Ordrenummer
    @FXML
    private TableColumn<Log, BaseOrder> tblColOrderNumberAdmin;

    //Navn på brugeren
    @FXML
    private TableColumn<Log, BaseUser> tblColName;

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
        tblColOrderNumberAdmin.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOrder()));
        tblColName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getUser()));
        tblColSignature.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAction().toString()));
        tblColDate.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        BackgroundTask.execute(
                () ->{
                    try {
                        logModel.reloadLogs();
                        return logModel.getAllLogs();
                    } catch (Exception e) {
                        throw new Error(e.getMessage());
                    }
                },
                list -> logTableView.setItems(list),
                error ->{
                    displayError("Problem", error.getMessage());
                    logTableView.setPlaceholder(new Label("Unable to get logs... try again later!"));
                },
                _ ->{
                    logTableView.setPlaceholder(new Label("Loading logs..."));
                }
        );
    }

    private boolean searchInLogs(Log log) {
        return false;
    }
}
