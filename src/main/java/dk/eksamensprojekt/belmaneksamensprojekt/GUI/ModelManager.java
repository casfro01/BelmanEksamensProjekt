package dk.eksamensprojekt.belmaneksamensprojekt.GUI;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.ReportModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.UserModel;

public class ModelManager {
    private static ModelManager instance;
    private final OrderModel orderModel;
    private final UserModel userModel;
    private final ReportModel reportModel;

    // models
    private ModelManager(){
        this.orderModel = new OrderModel();
        this.userModel = new UserModel();
        this.reportModel = new ReportModel();
    }

    public static ModelManager getInstance(){
        if(instance == null){
            instance = new ModelManager();
        }
        return instance;
    }

    // getters and setters
    public OrderModel getOrderModel(){ return orderModel; }
    public UserModel getUserModel() { return userModel; }
    public ReportModel getReportModel() { return reportModel; }
}
