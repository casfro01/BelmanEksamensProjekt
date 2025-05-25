package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.LoginUser;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;
import dk.eksamensprojekt.belmaneksamensprojekt.Constants.Constants;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDAO implements Repository<User, Integer>, UserData{

    @Override
    public List<User> getAll() throws Exception {
        List<User> users = new ArrayList<>();

        String sql = """
                SELECT * FROM [User];
                """;

        DBConnector db = new DBConnector();

        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String fullName = rs.getString("FullName");
                String email = rs.getString("Email");
                int role = rs.getInt("Role");

                User user = new User(id, role, email, fullName);

                String ImagePath = rs.getString("ImagePath");
                if (ImagePath != null) {
                    user.setImagePath(ImagePath);
                }

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
        String sql = """
                SELECT * FROM [User]
                WHERE ID = ?
                """;
        DBConnector db = new DBConnector();
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User(id, rs.getInt("Role"), rs.getString("FullName"), rs.getString("Email"));
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
    public void delete(User entity) {

    }

    @Override
    public void update(User entity) throws Exception {
        String sql = """
                UPDATE [User] SET FullName = ?, Email = ?, Role = ?
                WHERE ID = ?;
                """;
        DBConnector connector = new DBConnector();

        try (PreparedStatement ps = connector.getConnection().prepareStatement(sql)) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getEmail());
            ps.setInt(3, entity.getRole().toInt());
            ps.setInt(4, entity.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new Exception("Failed to update user: " + e.getMessage());
        }
    }

    @Override
    public User create(User user) throws Exception {
        String sql = """
               INSERT INTO [User] (FullName, Email, Role, ImagePath)
               VALUES (?, ?, ?, ?);
               """;
        DBConnector connector = new DBConnector();

        try (PreparedStatement ps = connector.getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setInt(3, user.getRole().toInt());
            ps.setString(4, user.getImagePath().equals(User.getBasicUserImage()) ? null : user.getImagePath());

            ps.executeUpdate();

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
        String sql = """
                SELECT ID, Email, Password FROM [User]
                WHERE Email = ?;
                """;
        DBConnector db = new DBConnector();
        try(PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
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
        String sql = """
                UPDATE [User] SET Password = ? WHERE Email = ?;
                """;

        DBConnector db = new DBConnector();
        try (PreparedStatement ps = db.getConnection().prepareStatement(sql)) {
            ps.setString(1, loginUser.getPassword());
            ps.setString(2, loginUser.getEmail());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new Exception("User creation failed: " + e.getMessage());
        }
    }
}
