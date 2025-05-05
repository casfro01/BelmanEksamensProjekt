package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Approved;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.BackgroundTask;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import dk.eksamensprojekt.belmaneksamensprojekt.Main;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML
    private TableColumn<Order, String> tblColOrderNumber;
    @FXML
    private TableColumn<Order, String> tblColApproved;
    @FXML
    private TableColumn<Order, Boolean> tblColDocumented;
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
        fillData();
    }

    private void fillData(){
        BackgroundTask.execute(
            () ->{ // hvad skal der ske
                // vil error ikke fange exception?
                try{
                    return orderModel.reloadOrderList();
                } catch (Exception e) {
                    ShowAlerts.displayMessage("Database Error", "Could not fetch orders: " + e.getMessage(), Alert.AlertType.ERROR);
                    return null;
                }
            },
            orders -> { // når tasken er færdiggjort
                // nothing ig -> siden det er loaded
                try {
                    Platform.runLater(this::createOrderForApprovalView);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    throw new RuntimeException(e);
                }
            },
            error -> { // hvis der sker en fejl
                orderTableView.setPlaceholder(new Label("Could not fetch data."));
                ShowAlerts.displayMessage("Database Error", "Could not fetch orders: " + error.getMessage(), Alert.AlertType.ERROR);
            },
            load -> { // imens vi venter
                if (load)
                    orderTableView.setPlaceholder(new Label("Loading..."));
                else
                    orderTableView.setPlaceholder(new Label("Orders found!"));
            }
        );
    }

    public void setupTableFiltering() {
        // hent ordre
        try {
            FilteredList<Order> filteredList = new FilteredList<>(orderModel.getOrderList(), p -> true);
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

            // TODO : REFACTOR ! - tænker ikke det er godt
            tblColOrderNumber.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderNumber()));
            tblColApproved.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isApproved().toString()));
            tblColDocumented.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isDocumented()));
            orderTableView.setItems(sortedList);
        } catch (Exception e) {
            ShowAlerts.displayMessage("Database Error", "Could not fetch orders: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // TODO : lav til listview og gør det der ig? eller så har jeg en anden ide -> Vbox ? -> new VBox(10, items1, item2, ...);
    // rename to load?
    private void createOrderForApprovalView() {
        // kan dette laves på en bedre måde?
        List<Order> todoOrders = orderModel.getOrdersForApproval();
        AnchorPane ap = new AnchorPane();
        //ap.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        /**
         * Fjen igen
         */
        /*
        todoOrders.add(new Order(1, "666-13106-017-3", null, Approved.NotReviewed));
        todoOrders.add(new Order(1, "666-13106-017-3", null, Approved.NotReviewed));
        todoOrders.add(new Order(1, "666-13106-017-3", null, Approved.NotReviewed));
        todoOrders.add(new Order(1, "666-13106-017-3", null, Approved.NotReviewed));
         */

        int counter = 0;
        int estiHeight = 65;
        int spacing = 10;
        for (Order o : todoOrders){
            AnchorPane orderPane = getOrderPane(o);
            ap.getChildren().add(orderPane);
            orderPane.setLayoutX(spacing);
            orderPane.setLayoutY(counter * (estiHeight + spacing * 2) + spacing);
            counter++;
        }

        //scrollPaneOrderApproval.getChildrenUnmodifiable().clear();
        ap.setStyle("-fx-background-color: #7FA8C5;");
        ap.setPrefSize(scrollPaneOrderApproval.getPrefWidth(), Region.USE_COMPUTED_SIZE + spacing * 2);
        scrollPaneOrderApproval.setContent(ap);
        scrollPaneOrderApproval.setStyle("-fx-background-color: #7FA8C5;");
    }

    private AnchorPane getOrderPane(Order o) {
        int spacing = 10;
        int estiHeight = 48;
        // base pane
        AnchorPane ap = new AnchorPane();
        ap.setPrefSize(scrollPaneOrderApproval.getPrefWidth() - spacing * 4, estiHeight + spacing * 2);
        ap.getStyleClass().add("orderItemPane");

        // label med order nummer
        Label lblOrderNumber = new Label(o.getOrderNumber());
        ap.getChildren().add(lblOrderNumber);
        lblOrderNumber.setLayoutX(spacing);
        lblOrderNumber.setLayoutY(spacing * 1.5f);
        lblOrderNumber.getStyleClass().addAll("orderItemText", "normalText");


        // den lille knap i siden
        ImageView iv = new ImageView();
        iv.setImage(new javafx.scene.image.Image(String.valueOf(Main.class.getResource("Icons/documentIcon.png"))));
        iv.setFitHeight(48);
        iv.setFitWidth(48);
        ap.getChildren().add(iv);
        //placering
        iv.setX(scrollPaneOrderApproval.getPrefWidth() - spacing * 10);
        iv.setY(spacing);
        // knap funktionalitet
        iv.setCursor(Cursor.HAND);
        iv.setOnMouseClicked(event -> {openDocumentWindow(o);});

        return ap;
    }


    private void openDocumentWindow(Order order){
        orderModel.setCurrentOrder(order);
        System.out.println("Jens");
        getInvoker().executeCommand(new SwitchWindowCommand(Windows.PreviewReportWindow));
    }
}
