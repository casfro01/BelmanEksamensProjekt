package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.LoginUser;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;

// Java imports
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Denne klasse håndterer bruger information, heriblandt base informationen og login oplysninger
 * Datakilden er microsoft-sql-database
 */
public class UserDAO implements Repository<User, Integer>, UserData{

    @Override
    public List<User> getAll() throws Exception {
        List<User> users = new ArrayList<>(); // returliste

        // sql-sætning til at hente data
        String sql = """
                SELECT * FROM [User];
                """;

        DBConnector db = new DBConnector();

        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            // Konstruere hver enkelte bruger, baseret på de rækker som kom tilbage
            while (rs.next()) {
                // base information
                int id = rs.getInt("ID");
                String fullName = rs.getString("FullName");
                String email = rs.getString("Email");
                int role = rs.getInt("Role");

                // lav bruger objekt
                User user = new User(id, role, email, fullName);

                // slæt profilbillede
                String ImagePath = rs.getString("ImagePath");
                if (ImagePath != null) {
                    user.setImagePath(ImagePath);
                }

                // tilføj brugeren til returlisten
                users.add(user);
            }
        }
        catch (Exception e) {
            throw new Exception("Failed to get all users: " + e.getMessage());
        }

        return users;

    }

    @Override
    public User getById(Integer id) throws Exception {
        // sql-sætning til at hente en række hvor et bestemt id er opfyldt
        String sql = """
                SELECT * FROM [User]
                WHERE ID = ?
                """;
        DBConnector db = new DBConnector();
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            // sæt id'et
            ps.setInt(1, id);
            // hent rækkerne
            ResultSet rs = ps.executeQuery();
            // hvis der kom noget tilbage
            if (rs.next()) {
                // udfyld informationerne i brugeren
                User user = new User(id, rs.getInt("Role"), rs.getString("FullName"), rs.getString("Email"));
                // sæt profilbillede
                String ImagePath = rs.getString("ImagePath");
                if (ImagePath != null) {
                    user.setImagePath(ImagePath);
                }
               return user;
            }
            return null;
        }
        catch (Exception e) {
            throw new Exception("Failed to get user: " + e.getMessage());
        }
    }

    @Override
    public void delete(User entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(User entity) throws Exception {
        // sql-sætning til at opdatere kolonner hvor id'et er mødt
        String sql = """
                UPDATE [User] SET FullName = ?, Email = ?, Role = ?
                WHERE ID = ?;
                """;
        DBConnector connector = new DBConnector();

        try (PreparedStatement ps = connector.getConnection().prepareStatement(sql)) {
            // sæt attributter
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getEmail());
            ps.setInt(3, entity.getRole().toInt());
            ps.setInt(4, entity.getId());

            // udfør
            ps.executeUpdate();

        } catch (Exception e) {
            throw new Exception("Failed to update user: " + e.getMessage());
        }
    }

    @Override
    public User create(User user) throws Exception {
        // sql-sætning til at lave en bruger, med basis informationer, som fulde navn, email osv...
        String sql = """
               INSERT INTO [User] (FullName, Email, Role, ImagePath)
               VALUES (?, ?, ?, ?);
               """;
        DBConnector connector = new DBConnector();

        try (PreparedStatement ps = connector.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            // sæt attributterne
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setInt(3, user.getRole().toInt());
            // sæt profilbillede
            ps.setString(4, user.getImagePath().equals(User.getBasicUserImage()) ? null : user.getImagePath());

            // udfør
            ps.executeUpdate();

            // hente den nye nøgle og send den tilbage med brugeren
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int newID = rs.getInt(1);
                return new User(newID, user.getRole(), user.getEmail(), user.getName());
            } else {
                throw new Exception("Failed to get userID");
            }
        } catch (Exception e) {
            throw new Exception("Failed to create new user: " + e.getMessage());
        }
    }

    @Override
    public LoginUser getLoginUser(String email) throws Exception{
        // sql-sætning til at hente login-informationer, ud fra en email
        String sql = """
                SELECT ID, Email, Password FROM [User]
                WHERE Email = ?;
                """;
        DBConnector db = new DBConnector();
        try(PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            // sæt email
            ps.setString(1, email);

            // hent brugeren
            ResultSet rs = ps.executeQuery();
            // hvis der er data
            if (rs.next()) {
                LoginUser lu = new LoginUser(rs.getInt(1), rs.getString(2), rs.getString(3));
                return lu;
            }
            return null;
        } catch (Exception e) {
            throw new Exception("Failed to get login user: " + e.getMessage());
        }
    }

    @Override
    public void createLoginUser(LoginUser loginUser) throws Exception {
        // sql-sætning til at indsætte brugerens login oplysninger -> password
        // (note -> man kunne også lave en tabel specifikt til brugerens login oplysninger)
        String sql = """
                UPDATE [User] SET Password = ? WHERE Email = ?;
                """;

        DBConnector db = new DBConnector();
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            // sæt password og email
            ps.setString(1, loginUser.getPassword());
            ps.setString(2, loginUser.getEmail()); // hvor den skal opdatere
            // gemme informationerne
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("User creation failed: " + e.getMessage());
        }
    }
}
