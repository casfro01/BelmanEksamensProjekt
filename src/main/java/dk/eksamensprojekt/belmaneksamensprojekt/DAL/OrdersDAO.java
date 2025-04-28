package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                Report r = new Report(rs.getInt(3), null);
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
    public void update(Order entity) {

    }

    @Override
    public void delete(Order entity) {

    }
}
