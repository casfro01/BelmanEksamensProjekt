package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Report;

import java.sql.*;
import java.util.List;

public class ReportDAO implements Repository<Report, Integer> {

    @Override
    public List<Report> getAll() throws Exception {
        return List.of();
    }

    @Override
    public Report getById(Integer integer) throws Exception {
        return null;
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
