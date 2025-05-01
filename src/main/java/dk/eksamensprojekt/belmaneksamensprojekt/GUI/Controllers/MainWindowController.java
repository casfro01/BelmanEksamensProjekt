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
    private TableView orderTableView;
    @FXML
    private ObservableList<Order> orderList;
    @FXML
    private TextField txtOrderSearchbar;

    public void setupTableFiltering() {
        FilteredList<Order> filteredList = new FilteredList<>(orderList, p -> true);
        txtOrderSearchbar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(order -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return order.getOrderNumber().toLowerCase().contains(lowerCaseFilter);

            });
        });

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
