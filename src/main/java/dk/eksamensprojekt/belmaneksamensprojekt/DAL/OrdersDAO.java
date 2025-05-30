package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.Approved;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.OrderType;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Report;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdersDAO implements Repository<Order, String>{

    @Override
    public List<Order> getAll() throws Exception{
        List<Order> orders = new ArrayList<>();
        String SQL = """
                SELECT Orders.ID, Orders.OrderNumber, Orders.Approve, Orders.ReportID, Orders.Documented, Orders.Size, Orders.OrderDate FROM Orders;
                """;
        DBConnector conn = new DBConnector();
        try(PreparedStatement ps = conn.getConnection().prepareStatement(SQL)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Report r = new Report(rs.getInt(4), null, null);
                Approved approvedEnum;
                boolean approvedColumn = rs.getBoolean(3);
                if (rs.wasNull()) {
                    approvedEnum = Approved.NOT_REVIEWED;
                } else if (approvedColumn) {
                    approvedEnum = Approved.APPROVED;
                } else {
                    approvedEnum = Approved.NOT_APPROVED;
                }

                Order current = new Order(rs.getInt(1), rs.getString(2), r, approvedEnum, rs.getBoolean(5));
                current.setOrderType(OrderType.getTypeFromIndex(rs.getInt(6)));
                current.setOrderDate(rs.getDate(7).toLocalDate());
                orders.add(current);
            }
        }
        catch(Exception e){
            throw new Exception("Failed to get all Orders: " + e.getMessage());
        }
        return orders;
    }

    /**
     * Denne metode bruger ordre nummeret som ligger
     * @param orderNumber
     * @return
     */
    @Override
    public Order getById(String orderNumber) throws Exception{
        String sql = """
                SELECT Orders.ID, Orders.ReportID, Orders.Approve, Orders.Documented, Orders.Size, Orders.OrderDate FROM Orders
                FULL OUTER JOIN Reports ON Orders.ReportID = Reports.ID
                WHERE Orders.OrderNumber = ?;
                """;
        DBConnector conn = new DBConnector();
        try(PreparedStatement ps = conn.getConnection().prepareStatement(sql)){

            // udfør
            ps.setString(1, orderNumber);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }

            boolean approvedColumn = rs.getBoolean(3);
            Approved apr = rs.wasNull() ? Approved.NOT_REVIEWED : Approved.valueOfBoolean(approvedColumn);

            // hvis rapport id er null / 0 så skal der ikke være nogen rapport
            int repID = rs.getInt(2);
            Report r = repID == 0 ? null : new Report(repID, null, null);

            Order order = new Order(rs.getInt(1), orderNumber, r, apr, rs.getBoolean(4));
            // sæt størrelse
            order.setOrderType(OrderType.getTypeFromIndex(rs.getInt(5)));
            // sæt dato
            order.setOrderDate(rs.getDate(6).toLocalDate());
            // returnerer
            return order;
        } catch (Exception e) {
            throw new Exception("Could not get order " + orderNumber + " because: " + e);
        }
    }

    @Override
    public Order create(Order entity) throws Exception{
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(Order entity) throws Exception {
        String sql = """
                UPDATE Orders SET Approve = ?, ReportID = ?, Documented = ? WHERE ID = ?;
                """;
        DBConnector db = new DBConnector();
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            // sæt rapport
            if (entity.getReport() == null || entity.getReport().getId() == 0)
                ps.setNull(2, Types.INTEGER);
            else
                ps.setInt(2, entity.getReport().getId());

            // set id
            ps.setInt(4, entity.getId());

            //Approve
            if (entity.isApproved() == Approved.NOT_REVIEWED)
                ps.setNull(1, Types.BIT); // vi har sat default til null, men dette gøres for at være op den sikre side
            else
                ps.setBoolean(1,entity.isApproved().toBoolean());
            //Documented
            ps.setBoolean(3, entity.isDocumented());
            ps.executeUpdate();
        }
            catch(SQLServerException e){
            throw new Exception("Failed to update Orders: " + e.getMessage());
            }
    }

    @Override
    public void delete(Order entity) throws Exception{
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

