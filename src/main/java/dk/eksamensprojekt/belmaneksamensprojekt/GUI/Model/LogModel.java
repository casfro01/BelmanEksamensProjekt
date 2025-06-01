package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Log;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.LogManager;

// JavaFX imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Denne model håndterer logs - specifikt for admins -> som skal se hvad der bliver lavet.
 */
public class LogModel {
    private ObservableList<Log> cachedLogs;
    private LogManager logManager;

    public LogModel() {
        this.cachedLogs = FXCollections.observableArrayList();
        this.logManager = new LogManager();
    }

    /**
     * Henter alle order og cacher dem (hvis der ikke allerede er komponenter i nuværende cache)
     * @return Liste med {@link Log}
     */
    public ObservableList<Log> getAllLogs() throws Exception {
        if (cachedLogs.isEmpty())
            cachedLogs.addAll(logManager.getAllLogs());
        return cachedLogs;
    }

    /**
     * Henter all logs fra databasen.
     */
    public void reloadLogs() throws Exception {
        cachedLogs.setAll(logManager.getAllLogs());
    }

    public Log getLogByID(Integer id) throws Exception {
        return logManager.getLogById(id);
    }

    /**
     * Gemmer en {@link Log} i datakilden.
     * @param log Den {@link Log} som skal gemmes i datakilden.
     */
    public void createLog(Log log) throws Exception {
        Log newLog = logManager.createLog(log);
        cachedLogs.add(newLog); // gemmer den nye log i cache-listen
    }
}



