package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import com.itextpdf.kernel.pdf.ReaderProperties;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Report;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.UserRole;

import java.sql.*;
import java.util.List;
import java.util.Random;

public class ReportDAO implements Repository<Report, Integer> {

    @Override
    public List<Report> getAll() throws Exception {
        return List.of();
    }

    @Override
    public Report getById(Integer id) throws Exception {
        String sql = """
                SELECT Reports.Path, [User].ID, [User].FullName, [User].Email, [User].Role FROM Reports
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

            // lav bruger
            User user = new User(rs.getInt(2), UserRole.fromInt(rs.getInt(5)), rs.getString(4), rs.getString(3));
            // lav rapport
            return new Report(id, rs.getString(1), user);
        }
        catch(Exception e){
            throw new Exception("Could not get report " + e.getMessage());
        }
    }

    @Override
    public Report create(Report entity) throws Exception {
        String sql = """ 
                INSERT INTO Reports (Path, UserID) VALUES (? , ?);
                """;
        DBConnector connector = new DBConnector();
        try (PreparedStatement ps = connector.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getReportPath());
            ps.setInt(2, entity.getUser().getId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                return new Report(id, entity.getReportPath(), entity.getUser());
            }
            return null;
        }
        catch (Exception e) {
            throw new Exception("Couldnt save report: " + e.getMessage());
        }
    }

    @Override
    public void update(Report entity) throws Exception {

    }

    @Override
    public void delete(Report entity) throws Exception {

    }
}
