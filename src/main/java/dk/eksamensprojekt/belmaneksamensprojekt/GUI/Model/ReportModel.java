package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Approved;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Image;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.ReportManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;

import java.util.List;

public class ReportModel {
    private final ReportManager reportManager;

    public ReportModel() {
        reportManager = new ReportManager();
    }

    public void saveReport(List<String> comments) throws Exception {
        Order order = ModelManager.getInstance().getOrderModel().getCurrentOrder();
        if (order != null) {
            ModelManager.getInstance().getOrderModel().updateOrder(order);
            order.getImageList().removeIf(image -> image.isApproved() == Approved.NotApproved);
            reportManager.saveReport(order, comments);
        } else {
            throw new Exception("Cant save report: No current order???");
        }
    }
}
