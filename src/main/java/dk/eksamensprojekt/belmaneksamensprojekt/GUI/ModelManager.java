package dk.eksamensprojekt.belmaneksamensprojekt.GUI;

import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.ReportModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.UserModel;

public enum ModelManager {
    INSTANCE;


    private final OrderModel ORDER_MODEL;
    private final UserModel USER_MODEL;
    private final ReportModel REPORT_MODEL;

    // models
    ModelManager(){
        this.ORDER_MODEL = new OrderModel();
        this.USER_MODEL = new UserModel();
        this.REPORT_MODEL = new ReportModel();
    }

    /*
    public static ModelManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ModelManager();
        }
        return INSTANCE;
    }
     */

    // getters and setters
    public OrderModel getOrderModel(){ return ORDER_MODEL; }
    public UserModel getUserModel() { return USER_MODEL; }
    public ReportModel getReportModel() { return REPORT_MODEL; }
}
