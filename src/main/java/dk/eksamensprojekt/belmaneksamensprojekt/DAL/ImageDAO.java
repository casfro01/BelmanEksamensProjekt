package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ImageDAO implements Repository<Image, Integer>, UpdateAll<Image> {
    @Override
    public List<Image> getAll() throws Exception {
        return List.of();
    }

    @Override
    public Image getById(Integer integer) throws Exception {
        return null;
    }

    @Override
    public Image create(Image image) throws Exception {
        String sql = """
                INSERT INTO Pictures (Path, UserID, OrderID, PicturePosition) VALUES (?, ?, ?, ?);
                """;
        DBConnector connector = new DBConnector();
        // siden at der ikke bliver lavet en forbindelse til db før man kalder getConnection - så er det fint det her
        try(PreparedStatement ps = connector.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, image.getPath());
            ps.setInt(2, image.getUser().getId());
            ps.setInt(3, image.getOrderID());
            ps.setInt(4, image.getImagePosition().toInt());

            ps.executeUpdate();

            // get keys -> to be returned
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return new Image(rs.getInt(1), image.getPath(), image.isApproved(), image.getUser(), image.getOrderID(), image.getImagePosition());
            }
        } catch (Exception e) {
            throw new Exception("Could not save image: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void update(Image image) throws Exception {
        String sql = """
                UPDATE Pictures SET Path = ?, UserID = ?, OrderID = ?
                WHERE ID = ?;
                """;
        DBConnector connector = new DBConnector();
        try(PreparedStatement ps = connector.getConnection().prepareStatement(sql)) {
            // set update parameters
            ps.setString(1, image.getPath());
            ps.setInt(2, image.getUser().getId());
            ps.setInt(3, image.getOrderID());

            // set where
            ps.setInt(4, image.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Could not update image" + e.getMessage());
        }
    }

    @Override
    public void updateAll(List<Image> images) throws Exception {
        String sql = """
                UPDATE Pictures SET Path = ?, UserID = ?, OrderID = ?, Approved = ?
                WHERE ID = ?;
                """;
        String sqlCreatepic = """
                INSERT INTO Pictures (Path, UserID, OrderID, PicturePosition) VALUES (?, ?, ?, ?);
                """;
        DBConnector connector = new DBConnector();
        try(Connection conn = connector.getConnection()) {
            try(PreparedStatement ps = conn.prepareStatement(sql);
                PreparedStatement psCreatepic = conn.prepareStatement(sqlCreatepic)){
                // start transaktion
                conn.setAutoCommit(false);
                // set update parameters
                for (Image image : images) {
                    //hvis den ikke har et ordrenummer
                    if (image.getOrderID() <= 0)
                        delete(image);
                    // hvis billedet allerede ligger i databasen
                    /*
                    else if (image.getId() > 0)
                        continue;
                     */

                    if (image.getId() > 0){
                        ps.setString(1, image.getPath());
                        ps.setInt(2, image.getUser().getId());
                        ps.setInt(3, image.getOrderID());
                        ps.setInt(4, image.getImagePosition().toInt());
                        if (image.isApproved() == Approved.NOT_REVIEWED)
                            ps.setNull(4, Types.BIT);
                        else
                            ps.setBoolean(4, image.isApproved().toBoolean());

                        // set hvor den skal opdateres
                        ps.setInt(5, image.getId());
                        ps.addBatch();
                    }
                    else{
                        psCreatepic.setString(1, image.getPath());
                        psCreatepic.setInt(2, image.getUser().getId());
                        psCreatepic.setInt(3, image.getOrderID());
                        psCreatepic.addBatch();
                    }

                }
                // udfør batch
                ps.executeBatch();
                psCreatepic.executeBatch();
                // gemme
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new Exception("Could not update image" + e.getMessage());
            }
            finally {
                // reset til standard
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            throw new Exception("Could not update image" + e.getMessage());
        }
    }

    public List<Image> getImageByOrder(Integer orderID) throws Exception {
        List<Image> images = new ArrayList<>();
        String sql = """
                SELECT Pictures.ID, Path, UserID, Approved, PicturePosition, [User].FullName, [User].Email, [User].Role FROM Pictures
                INNER JOIN [User] ON [User].ID = Pictures.UserID
                WHERE OrderID = ?;
                """;
        DBConnector connector = new DBConnector();
        try(PreparedStatement ps = connector.getConnection().prepareStatement(sql)) {
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // find user & lav user
                User u = new User(rs.getInt(3), rs.getInt(8), rs.getString(7), rs.getString(6));
                // find hvilken approved
                Approved app = Approved.valueOfBoolean(rs.getBoolean(4));
                if (rs.wasNull()) {
                    app = Approved.NOT_REVIEWED;
                }
                // lav billede objekt
                Image img = new Image(rs.getInt(1), rs.getString(2), app, u, orderID, ImagePosition.fromInt(rs.getInt(5)));
                images.add(img);
            }
        } catch (Exception e) {
            // idk man kunne lade vær
            throw new Exception("Could not get image by order: " + e.getMessage());
        }
        return images;
    }

    @Override
    public void delete(Image entity) throws Exception {
        String sql = """
                DELETE FROM Pictures WHERE ID = ?;
                """;
        DBConnector connector = new DBConnector();
        try(PreparedStatement ps = connector.getConnection().prepareStatement(sql)) {
            ps.setInt(1, entity.getId());
            ps.executeUpdate();
        }
        catch (Exception e) {
            throw new Exception("Could not delete image" + e.getMessage());
        }
    }
}
