package dk.eksamensprojekt.belmaneksamensprojekt.BLL;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Log;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.LogDAO;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.Repository;

// Java imports
import java.util.List;

/**
 * Manager klasse til at håndterer logs - bruges af gui-laget til at kommunikere med data-laget
 */
public class LogManager {

    private Repository<Log, Integer> logDao;

    public LogManager() {
        this.logDao = new LogDAO();
    }

    /**
     * Hent alle logs fra databasen.
     * @return Liste med alle logs fra databasen
     */
    public List<Log> getAllLogs() throws Exception{
        return logDao.getAll();
    }

    /**
     * Henter én specifik log baseret på sin primærnøgle i databasen
     * @param id Dét id som loggen har.
     * @return Den fulde log returneres baseret på det matchende id.
     */
    public Log getLogById(int id) throws Exception{
        return logDao.getById(id);
    }

    /**
     * Send en log ned til databasen for at blive gemt
     * @param log Dén log som skal gemmes
     * @return returnerer en log magen til den log som blev send ind, men med et id genereret af databasen.
     */
    public Log createLog(Log log) throws Exception{
        return logDao.create(log);
    }
}
