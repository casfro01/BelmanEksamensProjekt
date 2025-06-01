package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.UserRole;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchMainView;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Services.MainWindowService;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.MainWindowViews;

// JavaFX
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

// Java
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Denne kontroller håndterer det som sker for qc- og admin brugere.
 * Her kan disse brugere vælge mellem forskellige vinduer, hvor de kan lave alt fra at håndtere brugere
 * og logs.
 */
public class MainWindowController extends Controller implements Initializable {
    private OrderModel orderModel;
    private MainWindowService mainWindowService;

    //
    // JavaFX komponenter
    //
    @FXML
    private Label lblUser;
    @FXML
    private Label lblLogs;
    @FXML
    private AnchorPane showPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderModel = ModelManager.INSTANCE.getOrderModel();
        mainWindowService = new MainWindowService(this);

        // TODO : måske en bedre fiks
        orderModel.setCurrentOrder(null); // sætter den nuværende ordre til tom, for at den gamle ordre ikke vises i topbaren.

        isAdmin();
    }

    /**
     * Tjekker om brugeren er admin.
     * Dette gøres for at vise nogle af de handlinger som KUN admins skal kunne;
     * såsom at håndtere brugere.
     */
    private void isAdmin() {
        // hvis man er admin, så skal den vise vinduet med alle ordre først
        if (ModelManager.INSTANCE.getUserModel().getSelectedUser().getValue().getRole() == UserRole.ADMIN){
            lblUser.setVisible(true); // vise knap til brugere
            lblLogs.setVisible(true); // vise knap til logs
            Platform.runLater(() -> loadAllOrders(null));
        }
        else{ // hvis man er qc så skal den vise ordre som skal godkendes -> fordi det er deres job.
            Platform.runLater(() -> showOrderForApproval(null));
        }
    }

    @FXML
    private void manageUsers(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            getInvoker().executeCommand(new SwitchMainView(mainWindowService, MainWindowViews.UserWindow));
        }
    }

    @FXML
    private void manageLogs(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            getInvoker().executeCommand(new SwitchMainView(mainWindowService, MainWindowViews.AdminLogWindow));
        }
    }

    /**
     * Sætter det view, som er i midten af main-vindue.
     * @param anchorPane Viser det {@link AnchorPane} som bliver sendt ind.
     */
    public void setView(AnchorPane anchorPane){
        showPane.getChildren().clear();
        showPane.getChildren().add(anchorPane);

        // oprindelige størrelse (af vinduet).
        double width = 1920;
        double height = 1080;
        // tilføj hvad der er i showPanet
        initializeComponents(showPane, width, height);
        // gør det samme størrelse som alt andet
        resizeItems(Stage.getWindows().getFirst().getWidth(), Stage.getWindows().getFirst().getHeight());
    }

    @FXML
    private void loadAllOrders(MouseEvent mouseEvent) {
        getInvoker().executeCommand(new SwitchMainView(mainWindowService, MainWindowViews.AllOrdersWindow));
    }

    @FXML
    private void showOrderForApproval(MouseEvent mouseEvent) {
        getInvoker().executeCommand(new SwitchMainView(mainWindowService, MainWindowViews.OrderForApprovalWindow));
    }
}
