package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers.MainWindowControllers;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.BaseOrder;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.BaseUser;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Log;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.LogModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.BackgroundTask;

// JavaFX
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

// Java
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

// Statiske imports
import static dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts.displayError;

/**
 * Denne kontroller håndterer logs -> specifikt logs vinduet - på main vinduet.
 */
public class LogController extends Controller implements Initializable {

    private LogModel logModel;

    //
    // JavaFX komponenter
    //
    @FXML
    private TableView<Log> logTableView;

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logModel = ModelManager.INSTANCE.getLogModel();
        initializeTableView();
    }

    /**
     * Opsættelse af tableviewet - som viser alle logs + søgning
     */
    private void initializeTableView() {
        // opsættelse af kolonner
        tblColOrderNumberAdmin.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOrder()));
        tblColName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getUser()));
        tblColSignature.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAction().toString()));
        tblColDate.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        // hent logs -> på baggrunds tråd.
        BackgroundTask.execute(
                () ->{
                    try {
                        logModel.reloadLogs();
                        return logModel.getAllLogs(); // kast listen til onSuccess
                    } catch (Exception e) {
                        throw new Error(e.getMessage()); // kast fejl til onError
                    }
                },
                list -> { // sæt listen til tableviewet + opsættelse af søgning + sortering(indbygget i tableview)
                    FilteredList<Log> filteredList = new FilteredList<>(list);
                    txtOrderSearchbarAdmin.textProperty().addListener((observable, oldValue, newValue)
                            -> filteredList.setPredicate(log -> searchInLogs(log, newValue)));

                    // pak listen i sortedlist så man kan bruge sortering ift. kolonnerne i tableviewet
                    SortedList<Log> sortedList = new SortedList<>(filteredList);
                    logTableView.setItems(sortedList);},
                error ->{
                    displayError("Problem", error.getMessage());
                    logTableView.setPlaceholder(new Label("Unable to get logs... try again later!"));
                },
                _ -> logTableView.setPlaceholder(new Label("Loading logs..."))
        );
    }

    private boolean searchInLogs(Log log, String query) {
        return log.getOrder().getOrderNumber().startsWith(query);
    }
}
