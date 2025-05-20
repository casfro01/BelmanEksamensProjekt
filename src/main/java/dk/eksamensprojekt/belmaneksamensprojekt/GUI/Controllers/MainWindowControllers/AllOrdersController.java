package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers.MainWindowControllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.UserRole;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Providers.InvokerProvider;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.BackgroundTask;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import dk.eksamensprojekt.belmaneksamensprojekt.Main;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class AllOrdersController implements Initializable {
    private OrderModel orderModel;

    @FXML
    private TextField txtOrderSearchbar;
    @FXML
    private TableView<Order> orderTableView;
    @FXML
    private TableColumn<Order, String> tblColOrderNumber;
    @FXML
    private TableColumn<Order, String> tblColApproved;
    @FXML
    private TableColumn<Order, Boolean> tblColDocumented;
    @FXML
    private TableColumn<Order, Button> tblColAvailableReports;
    @FXML
    private TableColumn<Order, Button> colOrderInfo;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderModel = ModelManager.INSTANCE.getOrderModel();
        fillData();
    }

    private void fillData(){
        BackgroundTask.execute(
                () ->{ // hvad skal der ske
                    // vil error ikke fange exception?
                    try{
                        return orderModel.getOrderList();
                    } catch (Exception e) {
                        throw new Error(e);
                    }
                },
                orders -> { // når tasken er færdiggjort
                    Platform.runLater(this::setupTableFiltering);
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

            // sæt om ordren er dokumenteret - dette gøres med billeder og imageview
            tblColDocumented.setCellFactory(new Callback<>(){
                @Override
                public TableCell<Order, Boolean> call(TableColumn<Order, Boolean> param) {
                    return new TableCell<Order, Boolean>() {
                        protected void updateItem(Boolean item, boolean empty) {
                            if (empty)
                                setGraphic(null);
                            else{
                                // lav billede som skal sættes ind
                                ImageView valTrue = new ImageView();
                                valTrue.setFitHeight(25);
                                valTrue.setFitWidth(25);
                                setAlignment(Pos.CENTER);
                                // vælg billede
                                //Order jesus = getTableView().getItems().get(getIndex());
                                String imgString = getTableView().getItems().get(getIndex()).isDocumented() ? "Icons/true.png" : "Icons/false.png";
                                valTrue.setImage(new javafx.scene.image.Image(String.valueOf(Main.class.getResource(imgString))));
                                // sæt værdien
                                setGraphic(valTrue);
                            }
                        }
                    };
                }
            });
            // Sorterer ift. true og false -> dette gør det lidt langsomt, eller så er det fordi vi har billeder
            tblColDocumented.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isDocumented()));

            // set redigeringsknappen
            tblColAvailableReports.setCellFactory(new Callback<TableColumn<Order, Button>, TableCell<Order, Button>>() {

                @Override
                public TableCell<Order, Button> call(TableColumn<Order, Button> param) {
                    return new TableCell<Order, Button>() {
                        protected void updateItem(Button item, boolean empty) {
                            if (empty)
                                setGraphic(null);
                            else{
                                Order curOrder = getTableView().getItems().get(getIndex());
                                Button editButton = new Button("Edit");
                                editButton.setOnAction(event -> {
                                    editOrder(curOrder);
                                });
                                setGraphic(editButton);
                            }
                        }
                    };
                }
            });
            if (ModelManager.INSTANCE.getUserModel().getSelectedUser().getValue().getRole() != UserRole.ADMIN){
                colOrderInfo.setVisible(false);
            }
            // sæt ordrene ind
            orderTableView.setItems(sortedList);
        } catch (Exception e) {
            ShowAlerts.displayMessage("Database Error", "Could not fetch orders: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void editOrder(Order order){
        orderModel.setCurrentOrder(order);
        InvokerProvider.getInvoker().executeCommand(new SwitchWindowCommand(Windows.PreviewPicturesWindow));
    }
}
