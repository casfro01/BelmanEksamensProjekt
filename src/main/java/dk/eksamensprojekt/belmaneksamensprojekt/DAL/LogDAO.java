package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.BaseOrder;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.BaseUser;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Log;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.UserActions;

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
                SELECT Logs.*, u.FullName, o.OrderNumber FROM Logs
                INNER JOIN [User] u ON Logs.UserID = u.ID
                INNER JOIN Orders o ON Logs.OrderID = o.ID;
                """;
        DBConnector connector = new DBConnector();
    try (PreparedStatement ps = connector.getConnection().prepareStatement(sql)) {
        ps.setInt(1, 1);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            // attributter
            int id = rs.getInt("ID");
            int userId = rs.getInt("UserID");
            int orderId = rs.getInt("OrderID");
            String userName = rs.getString("FullName");
            String orderNumber = rs.getString("OrderNumber");
            LocalDate date = rs.getDate("Date").toLocalDate();
            UserActions action = UserActions.valueOfInt(rs.getInt("Action"));

            Log log = new Log(id, new BaseUser(userId, userName), action, date, new BaseOrder(orderId, orderNumber));
            logs.add(log);
        }
    }
        return logs;
    }

    @Override
    public Log getById(Integer id) throws Exception {
        String sql = """
                SELECT Logs.*, u.FullName, o.OrderNumber FROM Logs
                INNER JOIN [User] u ON Logs.UserID = u.ID
                INNER JOIN Orders o ON Logs.OrderID = o.ID
                WHERE Logs.ID = ?;
                """;
        DBConnector connector = new DBConnector();
        try (PreparedStatement ps = connector.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // attributter
                int userId = rs.getInt("UserID");
                int orderId = rs.getInt("OrderID");
                String userName = rs.getString("FullName");
                String orderNumber = rs.getString("OrderNumber");
                LocalDate date = rs.getDate("Date").toLocalDate();
                UserActions action = UserActions.valueOfInt(rs.getInt("Action"));

                return new Log(id, new BaseUser(userId, userName), action, date, new BaseOrder(orderId, orderNumber));
            }
        } catch (Exception e) {
            throw new Exception("Could not get log" + e.getMessage());
        }
        return null;
    }

    @Override
    public Log create(Log entity) throws Exception {
        String sql = """
                INSERT INTO Logs (UserID, OrderID, Date, Action) VALUES (?, ?, ?, ?);
                """;
        DBConnector connector = new DBConnector();
        try (PreparedStatement ps = connector.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getUser().getId());
            ps.setInt(2, entity.getOrder().getId());
            ps.setDate(3, Date.valueOf(entity.getDateTime()));
            ps.setInt(4, entity.getAction().toInt());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return new Log(rs.getInt(1), entity.getUser(), entity.getAction(), entity.getDateTime(), entity.getOrder());
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