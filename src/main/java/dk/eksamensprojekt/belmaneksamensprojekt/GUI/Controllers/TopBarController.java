package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Commands.SwitchWindowCommand;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.UserModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.Windows;
import dk.eksamensprojekt.belmaneksamensprojekt.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static dk.eksamensprojekt.belmaneksamensprojekt.BE.Image.IMAGES_PATH;

public class TopBarController extends Controller implements Initializable {
    private UserModel userModel;
    private OrderModel orderModel;
    @FXML
    private Label lblName;
    @FXML
    private Label lblRole;
    @FXML
    private Label lblCurrentOrder;
    @FXML
    private ImageView imgPicture;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // indsæt hvem der er logget ind
        userModel = ModelManager.INSTANCE.getUserModel();
        setUserInformation(userModel.getSelectedUser().get());

        // nuværende ordre
        orderModel = ModelManager.INSTANCE.getOrderModel();
        lblCurrentOrder.setText(orderModel.getCurrentOrder() == null ? "" : orderModel.getCurrentOrder().getOrderNumber());
    }

    private void setUserInformation(User user) {
        if (user != null) {
            lblName.setText(user.getName());
            lblRole.setText(user.getRole().toString());
            setImage(user);
        }
        else{
            lblRole.setText("");
            lblName.setText("");
            imgPicture.setVisible(false);
        }
    }

    private void setImage(User user){
        //User user = userModel.getSelectedUser().get();
        String path = user.getImagePath().equals(User.getBasicUserImage()) ? String.valueOf(Main.class.getResource(User.getBasicUserImage())) : "file:\\" + IMAGES_PATH + user.getImagePath();
        imgPicture.setPreserveRatio(false);
        imgPicture.setImage(new javafx.scene.image.Image(path));
    }

    @FXML
    private void logoutPressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY){
            // nulstil current order
            orderModel.setCurrentOrder(null);
            // do logout
            userModel.setSelectedUser(null); // måske ik?
            getInvoker().executeCommand(new SwitchWindowCommand(Windows.LoginWindow));
        }
    }

    // back metode
    @FXML
    private void backPressed(MouseEvent mouseEvent) {
        getInvoker().undoLastCommand();
    }
}
