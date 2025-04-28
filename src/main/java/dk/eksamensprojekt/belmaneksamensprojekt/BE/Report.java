package dk.eksamensprojekt.belmaneksamensprojekt.BE;

public class Report {
    private final String reportPath;
    private final int id;

    public Report(int id, String path) {
        this.id = id;
        this.reportPath = path;
    }

    public String getReportPath() {
        return reportPath;
    }
}
