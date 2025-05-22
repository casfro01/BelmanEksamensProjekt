package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Log;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LogDAO implements Repository<Log, Integer> {

    @Override
    public List<Log> getAll() throws Exception {
        List<Log> logs = new ArrayList<>();
        String sql = """
                SELECT * FROM Logs;
                """;
        DBConnector connector = new DBConnector();
    try (PreparedStatement ps = connector.getConnection().prepareStatement(sql)) {
        ps.setInt(1, 1);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("ID");
            int userId = rs.getInt("UserID");
            int orderId = rs.getInt("OrderID");
            LocalDate date = rs.getDate("Date").toLocalDate();
            Log log = new Log(id, userId, date, orderId);
            logs.add(log);
        }
    }
        return logs;
    }

    @Override
    public Log getById(Integer id) throws Exception {
        String sql = """
                SELECT * FROM Logs WHERE id = ?;
                """;
        DBConnector connector = new DBConnector();
        try (PreparedStatement ps = connector.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("UserID");
                int orderId = rs.getInt("OrderID");
                LocalDate date = rs.getDate("Date").toLocalDate();
                return new Log(id, userId, date, orderId);
            }
        } catch (Exception e) {
            throw new Exception("Could not get log" + e.getMessage());
        }
        return null;
    }

    @Override
    public Log create(Log entity) throws Exception {
        String sql = """
                INSERT INTO Logs (UserID, OrderID, Date) VALUES (?, ?, ?);
                """;
        DBConnector connector = new DBConnector();
        try (PreparedStatement ps = connector.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getuserID());
            ps.setInt(2, entity.getOrderID());
            ps.setDate(3, Date.valueOf(entity.getDateTime()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                Log log = new Log(rs.getInt("ID"), entity.getuserID(), entity.getDateTime(), entity.getOrderID());
                return log;
            }
        }
        return null;
    }

    @Override
    public void update(Log entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(Log entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}