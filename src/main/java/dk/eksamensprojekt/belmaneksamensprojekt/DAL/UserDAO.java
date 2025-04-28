package dk.eksamensprojekt.belmaneksamensprojekt.DAL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements Repository<User, Integer>{

    @Override
    public List<User> getAll() throws Exception {
        List<User> users = new ArrayList<>();

        String sql = """
                SELECT ID, FullName, Email, Role FROM users
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
            throw new Exception("Fail to get all users: " + e.getMessage());
        }

        return users;

    }

    @Override
    public User getById(Integer o) {
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
}
