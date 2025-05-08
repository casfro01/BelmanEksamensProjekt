package dk.eksamensprojekt.belmaneksamensprojekt.BLL;

public class LogManager {
    private LogDao logDao;

    public LogManager() {
        this.logDao = new LogDao();
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
