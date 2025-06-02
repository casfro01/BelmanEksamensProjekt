package dk.eksamensprojekt.belmaneksamensprojekt.BE;

import java.io.File;

/**
 * Denne klasse repræsenterer en rapport. Denne klasse KAN indeholde en byte-array som repræsenterer en pdf.
 * Hvis den har en byte-array har en også en bruger som har genereret den pdf.
 */
public class Report {
    //
    // Statiske konstanter
    //
    public static final String REPORTS_PATH = System.getProperty("user.dir") + File.separator + "Reports" + File.separator;

    //
    // Instans-variabler
    //
    private byte[] reportBlob;
    private final int id;
    private final User user;

    //
    // Constructors
    //
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

    //
    // Getters
    //
    public byte[] getReportBlob() { return reportBlob; }
    public User getUser() { return user; }
    public int getId() { return id; }

    //
    // Setters
    //
    public void setReportBlob(byte[] reportBlob) { this.reportBlob = reportBlob; }
}
