package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Log;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.LogManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LogModel {
    private ObservableList<Log> cachedLogs;
    private LogManager logManager;

    public LogModel() {
        this.cachedLogs = FXCollections.observableArrayList();
        this.logManager = new LogManager();
    }

    //Henter alle order og cacher dem
    public ObservableList<Log> getAllLogs() throws Exception {
        if (cachedLogs.isEmpty())
            cachedLogs.addAll(logManager.getAllLogs());
        return cachedLogs;
    }

    public Log getLogByID(Integer id) throws Exception {
        return logManager.getLogById(id);
    }
}



