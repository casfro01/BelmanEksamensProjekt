package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Report;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.ReportManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;

// Java
import java.io.File;
import java.util.List;

/**
 * Denne Model håndterer {@link Report}, og fungerer som mellemmand mellem BLL og GUI.
 */
public class ReportModel {
    private final ReportManager reportManager;

    public ReportModel() {
        reportManager = new ReportManager();
    }

    /**
     * Gem rapport tilstand i datakilden
     * @param comments Kommentarer til billederne
     */
    public void saveReport(List<String> comments) throws Exception {
        Order order = ModelManager.INSTANCE.getOrderModel().getCurrentOrder();
        if (order != null) {
            // ModelManager.getInstance().getOrderModel().updateOrder(order);
            reportManager.saveReport(order, comments);
        } else {
            throw new Exception("Cant save report: No current order???");
        }
    }

    public Report getReport(Integer id) throws Exception {
        return reportManager.getReport(id);
    }

    /**
     * Download rapporten
     * @param report Dén {@link Report} som skal downloades.
     * @return {@link File} repræsenterende som en pdf. Denne fil kan åbnes.
     */
    public File downloadReport(Report report) throws Exception {
        return reportManager.downloadReport(report);
    }
}
