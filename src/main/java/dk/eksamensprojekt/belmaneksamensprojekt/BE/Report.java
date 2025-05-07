package dk.eksamensprojekt.belmaneksamensprojekt.BE;

public class Report {
    private byte[] reportBlob;
    private final int id;
    private final User user;

    public Report(int id, byte[] reportBlob, User user) {
        this.id = id;
        this.reportBlob = reportBlob;
        this.user = user;
    }

    public Report(int id, User user) {
        this.id = id;
        this.reportBlob = null;
        this.user = user;
    }

    public byte[] getReportBlob() { return reportBlob; }
    public User getUser() { return user; }
    public int getId() {
        return id;
    }
    public void setReportBlob(byte[] reportBlob) {
        this.reportBlob = reportBlob;
    }
}
