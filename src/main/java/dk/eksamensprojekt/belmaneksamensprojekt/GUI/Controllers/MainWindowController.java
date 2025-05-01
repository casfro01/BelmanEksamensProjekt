package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import dk.eksamensprojekt.belmaneksamensprojekt.Main;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindowController extends Controller implements Initializable {

    private OrderModel orderModel;
    @FXML
    ScrollPane scrollPaneOrderApproval;
    @FXML
    private TableView<Order> orderTableView;
    /*
    @FXML
    private ObservableList<Order> orderList;
     */
    @FXML
    private TextField txtOrderSearchbar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderModel = ModelManager.getInstance().getOrderModel();
        //orderList = FXCollections.observableArrayList();
        setupTableFiltering();
        createOrderForApprovalView();
    }

    public void setupTableFiltering() {
        // lav filterliste
        FilteredList<Order> filteredList = new FilteredList<>(FXCollections.observableArrayList(), p -> true);
        // hent ordre
        try {
            filteredList.addAll(orderModel.getOrderList());
        } catch (Exception e) {
            ShowAlerts.displayMessage("Database Error", "Could not fetch orders: " + e.getMessage(), Alert.AlertType.ERROR);
        }
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

    private void createOrderForApprovalView() {
        // kan dette laves på en bedre måde?
        List<Order> todoOrders = orderModel.getOrdersForApproval();
        AnchorPane ap = new AnchorPane();
        ap.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        int counter = 0;
        int estiHeight = 65;
        int spacing = 10;
        for (Order o : todoOrders){
            AnchorPane orderPane = getOrderPane(o);
            ap.getChildren().add(orderPane);
            orderPane.setLayoutX(spacing);
            orderPane.setLayoutY(counter * (estiHeight + spacing));
            counter++;
        }
    }

    private AnchorPane getOrderPane(Order o) {
        int spacing = 10;
        int estiHeight = 65;
        // base pane
        AnchorPane ap = new AnchorPane();
        ap.setPrefSize(scrollPaneOrderApproval.getPrefWidth() - spacing * 2, estiHeight + spacing);
        ap.getStyleClass().add("orderItemPane");

        // label med order nummer
        Label lblOrderNumber = new Label(o.getOrderNumber());
        ap.getChildren().add(lblOrderNumber);
        lblOrderNumber.setLayoutX(spacing);
        lblOrderNumber.setLayoutY(spacing);
        lblOrderNumber.getStyleClass().addAll("orderItemText", "normalText");


        // den lille knap i siden
        ImageView iv = new ImageView();
        iv.setImage(new javafx.scene.image.Image(String.valueOf(Main.class.getResource("Icons/documentIcon.jpg"))));
        iv.setFitHeight(48);
        iv.setFitWidth(48);
        ap.getChildren().add(iv);
        //placering
        iv.setX(scrollPaneOrderApproval.getPrefWidth() - spacing * 4);
        iv.setY(spacing);
        // knap funktionalitet
        iv.setCursor(Cursor.HAND);
        iv.setOnMouseClicked(event -> {});

        return ap;
    }
}
