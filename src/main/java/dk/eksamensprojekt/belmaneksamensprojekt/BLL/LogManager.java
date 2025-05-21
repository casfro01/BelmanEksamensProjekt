package dk.eksamensprojekt.belmaneksamensprojekt.BLL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Log;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.LogDAO;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.Repository;

import java.util.List;

public class LogManager {

    private Repository<Log, Integer> logDao;

    public LogManager() {
        this.logDao = new LogDAO();
    }
    public List<Log> getAllLogs() throws Exception{
        return logDao.getAll();
    }
    public Log getLogById(int id) throws Exception{
        return logDao.getById(id);
    }

    public void createLog(Log log) throws Exception{
        logDao.create(log);
    }
}
