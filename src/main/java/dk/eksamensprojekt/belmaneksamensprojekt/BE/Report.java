package dk.eksamensprojekt.belmaneksamensprojekt.BE;

public class Report {
    private final String reportPath;
    private final int id;
    private final User user;

    public Report(int id, String path, User user) {
        this.id = id;
        this.reportPath = path;
        this.user = user;
    }

    public String getReportPath() { return reportPath; }
    public User getUser() { return user; }
    public int getId() {
        return id;
    }
}
