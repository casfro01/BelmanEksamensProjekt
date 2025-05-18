package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers.MainWindowControllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.BackgroundTask;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import dk.eksamensprojekt.belmaneksamensprojekt.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OrdersForApprovalController implements Initializable {

    private OrderModel orderModel;

    @FXML
    private ScrollPane scrollPaneOrderApproval;

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
                Platform.runLater(this::createOrderForApprovalView);
            },
            error -> { // hvis der sker en fejl
                ShowAlerts.displayMessage("Database Error", "Could not fetch orders: " + error.getMessage(), Alert.AlertType.ERROR);
            }
        );
    }


    // TODO : lav til listview og gør det der ig? eller så har jeg en anden ide -> Vbox ? -> new VBox(10, items1, item2, ...);
    // rename to load?
    private void createOrderForApprovalView() {
        // kan dette laves på en bedre måde?
        List<Order> todoOrders = orderModel.getOrdersForApproval();
        AnchorPane ap = new AnchorPane();
        //ap.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

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
        ap.setStyle("-fx-background-color: #c7c7c7;");
        ap.setPrefSize(scrollPaneOrderApproval.getPrefWidth(), Region.USE_COMPUTED_SIZE + spacing * 2);
        ap.setMinHeight(scrollPaneOrderApproval.getPrefHeight());
        scrollPaneOrderApproval.setContent(ap);
        scrollPaneOrderApproval.setStyle("-fx-background-color: #c7c7c7;");
        scrollPaneOrderApproval.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneOrderApproval.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
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

    }
}
