package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Report;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.UserRole;

// Java imports
import java.sql.*;
import java.util.List;

/**
 * Denne klasse håndterer rapportens informationer.
 * Datakilden er microsoft-sql-database.
 */
public class ReportDAO implements Repository<Report, Integer> {

    @Override
    public List<Report> getAll() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Report getById(Integer id) throws Exception {
        // sql-sætning til at hente rapportens informationer baseret på et id.
        String sql = """
                SELECT Reports.ReportBlob, [User].ID, [User].FullName, [User].Email, [User].Role FROM Reports
                INNER JOIN [User] ON Reports.UserID = [User].ID
                WHERE Reports.ID = ?;
                """;
        DBConnector connector = new DBConnector();
        try(PreparedStatement ps = connector.getConnection().prepareStatement(sql)){
            // hente data
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            // hvis der ikke er noget
            if (!rs.next())
                return null;

            // lav bruger ->
            // kan ændres til BaseUser, men dette er lavet før den var en ting
            User user = new User(rs.getInt(2), UserRole.fromInt(rs.getInt(5)), rs.getString(4), rs.getString(3));
            // lav rapport
            return new Report(id, rs.getBytes(1), user);
        }
        catch(Exception e){
            throw new Exception("Could not get report " + e.getMessage());
        }
    }

    @Override
    public Report create(Report entity) throws Exception {
        // sql-sætning til at indsætte en rapport i databasen
        String sql = """ 
                INSERT INTO Reports (ReportBlob, UserID) VALUES (? , ?);
                """;
        DBConnector connector = new DBConnector();
        try (PreparedStatement ps = connector.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // indsæt data-blob
            ps.setBytes(1, entity.getReportBlob());
            // indsæt brugerens id
            ps.setInt(2, entity.getUser().getId());
            // udfør
            ps.executeUpdate();
            // hent ny nøgle og send tilbage
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                return new Report(id, entity.getReportBlob(), entity.getUser());
            }
            return null;
        }
        catch (Exception e) {
            throw new Exception("Couldnt save report: " + e.getMessage());
        }
    }

    @Override
    public void update(Report entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Report entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
