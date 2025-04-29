package dk.eksamensprojekt.belmaneksamensprojekt.GUI;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;

public class ModelManager {
    private static ModelManager instance;
    private final OrderModel orderModel;

    // models
    private ModelManager(){
        this.orderModel = new OrderModel();
    }

    public static ModelManager getInstance(){
        if(instance == null){
            instance = new ModelManager();
        }
        return instance;
    }

    // getters and setters
    public OrderModel getOrderModel(){
        return orderModel;
    }
}
