package dk.eksamensprojekt.belmaneksamensprojekt.GUI.util;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.UserActions;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Log;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.UserModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;

import java.time.LocalDate;

public class LogCreatorHelper {
    private static OrderModel orderModel;
    private static UserModel userModel;

    public static Log logFactory(UserActions action){
        if (orderModel == null){
            orderModel = ModelManager.INSTANCE.getOrderModel();
        }
        if (userModel == null){
            userModel = ModelManager.INSTANCE.getUserModel();
        }
        return new Log(-1, userModel.getSelectedUser().get(), action, LocalDate.now(), orderModel.getCurrentOrder());
    }
}
