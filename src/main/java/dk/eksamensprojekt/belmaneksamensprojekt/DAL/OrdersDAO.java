package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Approved;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class OrdersDAO implements Repository<Order, Integer>{

    @Override
    public List<Order> getAll() throws Exception{
        List<Order> orders = new ArrayList<>();
        String SQL = """
                SELECT ID, OrderNumber, Order.ReportID FROM Orders
                INNER JOIN Report ON Order.ReportID = Report.ID;
                """;
        DBConnector conn = new DBConnector();
        try(PreparedStatement ps = conn.getConnection().prepareStatement(SQL)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                // tilføj billeder her lil' skat
                Report r = new Report(rs.getInt(3), null, null);
                Order current = new Order(rs.getInt(1), rs.getString(2), r);
                orders.add(current);
            }
        }
        catch(Exception e){
            throw new Exception("Failed to get all Orders: " + e.getMessage());
        }
        return orders;
    }

    @Override
    public Order getById(Integer integer) {
        return null;
    }

    @Override
    public Order create(Order entity) {
        return null;
    }

    @Override
    public void update(Order entity) throws Exception {
        String sql = """
                UPDATE Orders SET Approve = ? ReportID = ? WHERE ID = ?";"
                """;
        DBConnector db = new DBConnector();
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(2, entity.getReport().getId());
            ps.setInt(3, entity.getId());

            if (entity.isApproved() == Approved.NotReviewed)
                ps.setNull(1, Types.BIT);
            else
                ps.setBoolean(1,entity.isApproved().toBoolean());
            ps.executeQuery();

        }
            catch(SQLServerException e){
            throw new Exception("Failed to update Orders: " + e.getMessage());
            }
    }

    @Override
    public void delete(Order entity) {

    }

}

