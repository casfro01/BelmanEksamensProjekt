package dk.eksamensprojekt.belmaneksamensprojekt.GUI.util;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.UserActions;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Log;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.UserModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;

// Java imports
import java.time.LocalDate;

/**
 * Denne klasse kunne fungere som et factory, da den hjælper med at lave en log ud fra en {@link UserActions}.
 */
public class LogCreatorHelper {
    private static OrderModel orderModel;
    private static UserModel userModel;

    /**
     * Laver en {@link Log} baseret på en {@link UserActions}
     * @param action Den {@link UserActions} som brugeren har lavet
     * @return {@link Log} med den {@link UserActions} som blev sendt ind
     */
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
