package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.*;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.Approved;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.ImagePosition;

// Java imports
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Denne klasse håndterer dataoperationer med en datakilde.
 * Denne datakilde er en microsoft sql-server.
 */
public class ImageDAO implements Repository<Image, Integer>, UpdateAll<Image> {
    @Override
    public List<Image> getAll() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Image getById(Integer integer) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Image create(Image image) throws Exception {
        // Indsætter data i Pictures-tabellen -> her indgår ting som stien til billedet,
        // hvem der har taget billedet og hvilken vinkel billedet repræsentere.
        String sql = """
                INSERT INTO Pictures (Path, UserID, OrderID, PicturePosition) VALUES (?, ?, ?, ?);
                """;
        DBConnector connector = new DBConnector();
        // siden at der ikke bliver lavet en forbindelse til db før man kalder getConnection - så er det fint det her
        try(PreparedStatement ps = connector.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Udfyld parametrene -> (denne måde undgår sql injection)
            ps.setString(1, image.getPath());
            ps.setInt(2, image.getUser().getId());
            ps.setInt(3, image.getOrderID());
            ps.setInt(4, image.getImagePosition().toInt());

            // Udfør sql-sætningen
            ps.executeUpdate();

            // hent keys -> to be returned
            ResultSet rs = ps.getGeneratedKeys();

            // Hvis den blev oprettet, så sender vi et nyt billede tilbage med det nyligt genereret id.
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
        // Opdater et enkelt billede, i de kolonner som er nødvendig
        String sql = """
                UPDATE Pictures SET Path = ?, UserID = ?, OrderID = ?, Approved = ?
                WHERE ID = ?;
                """;
        DBConnector connector = new DBConnector();
        try(PreparedStatement ps = connector.getConnection().prepareStatement(sql)) {
            // sæt update parametre
            ps.setString(1, image.getPath());
            ps.setInt(2, image.getUser().getId());
            ps.setInt(3, image.getOrderID());
            ps.setBoolean(4, image.isApproved().toBoolean());

            // sæt hvor den skal opdatere
            ps.setInt(5, image.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Could not update image" + e.getMessage());
        }
    }

    @Override
    public void updateAll(List<Image> images) throws Exception {
        // Hvis billedet allerede findes i databasen, så skal denne sql-sætning bruges
        String sql = """
                UPDATE Pictures SET Path = ?, UserID = ?, OrderID = ?, Approved = ?
                WHERE ID = ?;
                """;
        // Hvis billedet ikke findes i databasen, så skal denne sql-sætning bruges
        String sqlCreatepic = """
                INSERT INTO Pictures (Path, UserID, OrderID, PicturePosition) VALUES (?, ?, ?, ?);
                """;
        DBConnector connector = new DBConnector();
        try(Connection conn = connector.getConnection()) {
            try(PreparedStatement ps = conn.prepareStatement(sql);
                PreparedStatement psCreatepic = conn.prepareStatement(sqlCreatepic)){
                // start transaktion -> hvis nu der går noget galt undervejs
                conn.setAutoCommit(false);
                // sæt update parametre
                for (Image image : images) {
                    // hvis den ikke har et ordrenummer -> så er den fjernet
                    if (image.getOrderID() <= 0)
                        delete(image);

                    // Hvis billedet allerede ligger i databasen
                    // -> Den første sql-sætning bruges
                    if (image.getId() > 0){
                        ps.setString(1, image.getPath());
                        ps.setInt(2, image.getUser().getId());
                        ps.setInt(3, image.getOrderID());
                        if (image.isApproved() == Approved.NOT_REVIEWED)
                            ps.setNull(4, Types.BIT);
                        else
                            ps.setBoolean(4, image.isApproved().toBoolean());

                        // set hvor den skal opdateres
                        ps.setInt(5, image.getId());
                        ps.addBatch();
                    }
                    else{ // Hvis billedet ikke er i databasen -> anden sql-sætning bruges
                        psCreatepic.setString(1, image.getPath());
                        psCreatepic.setInt(2, image.getUser().getId());
                        psCreatepic.setInt(3, image.getOrderID());
                        psCreatepic.setInt(4, image.getImagePosition().toInt());
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
                throw new Exception("Could not update image " + e.getMessage());
            }
            finally {
                // reset til standard igen
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            throw new Exception("Could not update image" + e.getMessage());
        }
    }

    /**
     * Hent billeder til en bestemt ordre.
     * @param orderID Det ordre id som billederne er tilknyttet til.
     * @return Liste med billeder tilhørende det ordre id
     */
    public List<Image> getImageByOrder(Integer orderID) throws Exception {
        List<Image> images = new ArrayList<>(); // retur liste
        // Henter billede + brugeren som har lavet billedet
        String sql = """
                SELECT Pictures.ID, Path, UserID, Approved, PicturePosition, [User].FullName, [User].Email, [User].Role FROM Pictures
                INNER JOIN [User] ON [User].ID = Pictures.UserID
                WHERE OrderID = ?;
                """;
        DBConnector connector = new DBConnector();
        try(PreparedStatement ps = connector.getConnection().prepareStatement(sql)) {
            ps.setInt(1, orderID); // udfyld information
            ResultSet rs = ps.executeQuery(); // hent information
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
            throw new Exception("Could not get image by order: " + e.getMessage());
        }
        return images;
    }

    @Override
    public void delete(Image entity) throws Exception {
        // sql-sætning til at slette rækker, hvor id'et er opfyldt
        String sql = """
                DELETE FROM Pictures WHERE ID = ?;
                """;
        DBConnector connector = new DBConnector();
        try(PreparedStatement ps = connector.getConnection().prepareStatement(sql)) {
            // indsæt parameter
            ps.setInt(1, entity.getId());
            // udfør
            ps.executeUpdate();
        }
        catch (Exception e) {
            throw new Exception("Could not delete image" + e.getMessage());
        }
    }
}
