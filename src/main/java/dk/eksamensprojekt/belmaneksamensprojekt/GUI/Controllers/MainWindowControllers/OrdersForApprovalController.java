package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers.MainWindowControllers;

// projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Providers.InvokerProvider;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.BackgroundTask;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import dk.eksamensprojekt.belmaneksamensprojekt.Main;

import static dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts.displayError;

// JavaFX
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

// Java
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Denne kontroller håndterer ordre som skal godkendes - som ligger på main-vinduet.
 */
public class OrdersForApprovalController implements Initializable {
    private OrderModel orderModel;

    //
    // JavaFX komponenter
    //
    @FXML
    private ScrollPane scrollPaneOrderApproval;
    @FXML
    private TextField txtSearchOrderApproval;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderModel = ModelManager.INSTANCE.getOrderModel();

        fillData();

        initializeSearchbar();
    }

    /**
     * Udfyld dataet
     */
    private void fillData(){
        BackgroundTask.execute(
            () ->{ // hvad skal der ske
                try{
                    return orderModel.getOrderList(); // kast til onSuccess
                } catch (Exception e) {
                    throw new Error(e); // kast til onError
                }
            },
            orders -> { // når tasken er færdiggjort
                Platform.runLater(() -> createOrderForApprovalView(""));
            },
            error -> { // hvis der sker en fejl
                displayError("Database Error", "Could not fetch orders: " + error.getMessage());
            }
        );
    }


    /**
     * Laver elementerne på i ordersForApproval boksen.
     * @param filter Søge filteret / søge ord -> når der skal søges i ordrene.
     */
    // TODO : lav til listview og gør det der ig? eller så har jeg en anden ide -> Vbox ? -> new VBox(10, items1, item2, ...);
    private void createOrderForApprovalView(String filter) {
        List<Order> todoOrders = orderModel.getOrdersForApproval(); // hent ordre for approval

        // søge -> men det kan helt klart gøres bedre -> dette er den første og nemmeste. todo -> forbedre
        List<Order> actual = new ArrayList<>();
        for (Order order : todoOrders){
            if (order.getOrderNumber().startsWith(filter)){
                actual.add(order);
            }
        }
        todoOrders = actual;

        AnchorPane ap = new AnchorPane();
        //ap.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        // sortere efter bestillings dag, for at få de ældste øverst
        todoOrders.sort(Comparator.comparing(Order::getOrderDate));

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

        // indsætter anchorPanet i scrollPanet.
        ap.setStyle("-fx-background-color: #c7c7c7;");
        ap.setPrefSize(scrollPaneOrderApproval.getPrefWidth(), Region.USE_COMPUTED_SIZE + spacing * 2);
        ap.setMinHeight(scrollPaneOrderApproval.getPrefHeight());
        scrollPaneOrderApproval.setContent(ap);
        scrollPaneOrderApproval.setStyle("-fx-background-color: #c7c7c7;");
        scrollPaneOrderApproval.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneOrderApproval.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }


    /**
     * Laver et {@link AnchorPane} for en ordre.
     * @param o Den {@link Order} som skal have et {@link AnchorPane}, hvor dets informationer vises.
     * @return Det {@link AnchorPane} som blev genereret baseret på ordren.
     */
    private AnchorPane getOrderPane(Order o) {
        int spacing = 10;
        int estiHeight = 48;
        // base pane
        AnchorPane ap = new AnchorPane();
        ap.setPrefSize(scrollPaneOrderApproval.getPrefWidth() - spacing * 4, estiHeight + spacing * 2);
        ap.getStyleClass().add("orderItemPane");

        // hvis ordren er en måned gammel (30 dage -> antal dage inde nu - antal dage da den blev oprettet)
        if (LocalDate.now().getDayOfYear() - o.getOrderDate().getDayOfYear() >= 30)
            ap.getStyleClass().add("notApproved");

        // label med order nummer
        Label lblOrderNumber = new Label(o.getOrderNumber());
        ap.getChildren().add(lblOrderNumber);
        lblOrderNumber.setLayoutX(spacing);
        lblOrderNumber.setLayoutY(spacing * 1.5f);
        lblOrderNumber.getStyleClass().addAll("orderItemText", "normalText");

        // label med ordre bestilling dato
        Label lblOrderDate = new Label(o.getOrderDate().format(DateTimeFormatter.ofPattern(Order.DATE_FORMAT)));
        ap.getChildren().add(lblOrderDate);
        lblOrderDate.setLayoutX(ap.getPrefWidth() / 2);
        lblOrderDate.setLayoutY(spacing * 1.5f);
        lblOrderDate.getStyleClass().addAll("orderItemText", "normalText");

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

    /**
     * Åbner "Preview pictures" vinduet, hvor brugeren kan godkende ordrens informationer
     * @param order Den {@link Order} som vinduet skal åbne for.
     */
    private void openDocumentWindow(Order order){
        orderModel.setCurrentOrder(order);
        InvokerProvider.getInvoker().executeCommand(new SwitchWindowCommand(Windows.PreviewPicturesWindow));
    }

    /**
     * Opsætter søgebaren.
     */
    private void initializeSearchbar() {
        txtSearchOrderApproval.textProperty().addListener((observable, oldValue, newValue) -> {
            createOrderForApprovalView(newValue);
        });
    }
}
