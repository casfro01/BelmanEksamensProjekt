package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.BackgroundTask;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;

// JavaFX
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

// Java
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Denne kontroller håndterer ordrenummer-indtastning for operatøren -> (hjælper person med at skrive det ind).
 */
public class OperatorWindowController extends Controller implements Initializable {
    // måske til bll / lav en verifier
    private static final String regex = "^\\d{3}-\\d{5}-\\d{3}-\\d$";
    private Pattern pattern;
    private ModelManager modelManager;
    private OrderModel orderModel;

    //
    // JavaFX komponenter
    //
    @FXML
    private TextField txtSearchOrdernumb;
    @FXML
    private ListView<Order> lstOrders;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pattern = Pattern.compile(regex);
        modelManager = ModelManager.INSTANCE;
        orderModel = modelManager.getOrderModel();

        // TODO : måske en bedre fiks
        orderModel.setCurrentOrder(null);


        initializeListView();
    }

    /**
     * Opsætter listviewet -> så den kan vise alle ordre for operatøren, så det er nemmere for brugeren
     * at benytte.
     */
    private void initializeListView() {
        // load liste
        BackgroundTask.execute(
            () -> {
                try {
                    return new FilteredList<>(orderModel.getOrderList());
                } catch (Exception e) {
                    throw new Error(e);
                }
            },
            orders -> {
                Platform.runLater(() -> {
                    // tilføj lytter til søgefelt
                    txtSearchOrdernumb.textProperty().addListener((observable, oldValue, newValue) -> {
                        orders.setPredicate(order -> filterOrder(order, newValue));
                    });
                    // indsæt listen
                    lstOrders.setItems(orders);});
            },
            error ->{
                Platform.runLater(() -> ShowAlerts.displayError("Database error", "Could not fetch orders: " + error.getMessage()));
            },
            loading -> {
                Platform.runLater(() ->{
                    lstOrders.setPlaceholder(new Label("Loading orders..."));
                });
            }
        );

        // når man klikker på en ordre
        lstOrders.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (lstOrders.getSelectionModel().getSelectedItem() != null){
                orderModel.setCurrentOrder(lstOrders.getSelectionModel().getSelectedItem());
                getInvoker().executeCommand(new SwitchWindowCommand(Windows.PhotoDocWindow));
            }
        });
    }

    /**
     * Fortæller om en {@link Order}'s ordrenummer passer med søgetermen.
     * @param order Den {@link Order} som skal "undersøges"
     * @param query Søgeterm if form af {@link String}
     * @return true eller false
     */
    private boolean filterOrder(Order order, String query){
        return order.getOrderNumber().startsWith(query);
    }

    @FXML
    private void onEnterPressed(KeyEvent keyEvent) throws Exception {
        if (keyEvent.getCode() == KeyCode.ENTER){
           searchOrder();
        }
    }

    /**
     * Tjekker om det indtastede passer med hvordan et ordrenummer er formateret
     */
    private void searchOrder() throws Exception {
        String txt = txtSearchOrdernumb.getText();
        if (txt.isEmpty()) {
            return;
        }

        if (!pattern.matcher(txt).matches()) {
            throw new Exception("Order id doesnt match regex");
        }

        Order order = orderModel.searchOrder(txt);
        orderModel.setCurrentOrder(order);
        getInvoker().executeCommand(new SwitchWindowCommand(Windows.PhotoDocWindow));
    }

    @FXML
    private void searchOrderPressed(MouseEvent mouseEvent) {
        try {
            searchOrder();
        } catch (Exception e) {
            ShowAlerts.displayError("Invalid Order", "Invalid order number " + e.getMessage());
        }
    }
}
