package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController extends Controller implements Initializable {

    @FXML
    private TableView<Order> orderTableView;
    @FXML
    private ObservableList<Order> orderList;
    @FXML
    private TextField txtOrderSearchbar;

    public void setupTableFiltering() {
        FilteredList<Order> filteredList = new FilteredList<>(orderList, p -> true);
        txtOrderSearchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(order -> {
                // Hvis den er tom eller hvis der ikke er noget i søgefeltet -> så skal de vises
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                //String lowerCaseFilter = newValue.toLowerCase(); tal til lowercase?
                // TODO : skal man også bare kunne søge på et ordrenummer sådan 128487523 istedet + begge? for 123-85433-.... ?
                return order.getOrderNumber().startsWith(newValue);
                //return order.getOrderNumber().toLowerCase().contains(lowerCaseFilter); -> toLowercase ikke nødvendigt -> det er tal ... blev vi ikke lige enige om at det var startswith?

            });
        });

        // opsætning af sortering
        SortedList<Order> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(orderTableView.comparatorProperty());
        orderTableView.setItems(sortedList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderList = FXCollections.observableArrayList();
        setupTableFiltering();
    }
}
