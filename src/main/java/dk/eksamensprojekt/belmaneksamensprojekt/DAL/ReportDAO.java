package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Report;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.UserRole;

import java.sql.*;
import java.util.List;

public class ReportDAO implements Repository<Report, Integer> {

    @Override
    public List<Report> getAll() throws Exception {
        return List.of();
    }

    @Override
    public Report getById(Integer id) throws Exception {
        System.out.println(id);
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

            // lav bruger
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
        String sql = """ 
                INSERT INTO Reports (ReportBlob, UserID) VALUES (? , ?);
                """;
        DBConnector connector = new DBConnector();
        try (PreparedStatement ps = connector.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBytes(1, entity.getReportBlob());
            ps.setInt(2, entity.getUser().getId());
            ps.executeUpdate();
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

        return null;
    }

    @Override
    public void delete(Report entity) throws Exception {

    }
}
