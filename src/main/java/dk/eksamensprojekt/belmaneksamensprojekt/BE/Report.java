package dk.eksamensprojekt.belmaneksamensprojekt.BE;

import java.util.List;

public class Report {
    //private List<Image> images;
    private final String reportPath;
    private final int id;

    public Report(int id, String path) {
        //this.images = images;
        this.id = id;
        this.reportPath = path;
    }

    /* public List<Image> getImages() {
        return images;
    }
     */
    public String getReportPath() {
        return reportPath;
    }
}
