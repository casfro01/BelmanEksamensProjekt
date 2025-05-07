package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.LoginUser;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements Repository<User, Integer>, UserData{

    @Override
    public List<User> getAll() throws Exception {
        List<User> users = new ArrayList<>();

        String sql = """
                SELECT ID, FullName, Email, Role FROM [User];
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
                users.add(user);
            }
        }
        catch (Exception e) {
            throw new Exception("Failed to get all users: " + e.getMessage());
        }

        return users;

    }

    @Override
    public User getById(Integer id) {
        return null;
    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public void update(User entity) {

    }

    @Override
    public User create(User entity) {
        return null;
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
}
