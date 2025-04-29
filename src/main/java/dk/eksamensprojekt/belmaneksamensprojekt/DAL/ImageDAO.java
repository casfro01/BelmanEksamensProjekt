package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Image;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class ImageDAO implements Repository<Image, Integer>{
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
                INSERT INTO Pictures (Path, UserID, OrderID) VALUES (?, ?, ?);
                """;
        DBConnector connector = new DBConnector();
        // siden at der ikke bliver lavet en forbindelse til db før man kalder getConnection - så er det fint det her
        try(PreparedStatement ps = connector.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, image.getPath());
            ps.setInt(2, image.getUser().getId());
            ps.setInt(3, image.getOrderID());

            ps.executeUpdate();

            // get keys -> to be returned
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return new Image(rs.getInt(1), image.getPath(), image.isApproved(), image.getUser(), image.getOrderID());
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
    public void delete(Image entity) throws Exception {

    }
}
