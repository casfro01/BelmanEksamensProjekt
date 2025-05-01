package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.ReportManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;

import java.util.List;

public class ReportModel {
    private final ReportManager reportManager;
    private final ModelManager modelManager;

    public ReportModel() {
        reportManager = new ReportManager();
        modelManager = ModelManager.getInstance();
    }

    public void saveReport(List<String> comments) throws Exception {
        Order order = modelManager.getOrderModel().getCurrentOrder();
        if (order != null) {
            reportManager.saveReport(order, comments);
        } else {
            throw new Exception("Cant save report: No current order???");
        }
    }
}
