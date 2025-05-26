package dk.eksamensprojekt.belmaneksamensprojekt.BLL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.LoginUser;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.util.EmailValidator;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.util.IHashing;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.util.PasswordHasher;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.Repository;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.UserDAO;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.UserData;

import javax.management.relation.Role;
import java.util.List;
import java.util.regex.Pattern;

public class UserManager {
    private final Repository<User, Integer> userDAO;
    private final IHashing passwordHashing;

    public UserManager() {
        this.userDAO = new UserDAO();
        this.passwordHashing = new PasswordHasher();
    }

    public List<User> getAllUsers() throws Exception {
        return userDAO.getAll();
    }

    public User login(String email, String password) throws Exception {
        // validér emailen
        if (!EmailValidator.validate(email))
            throw new IllegalArgumentException("Invalid email address");

        if (userDAO instanceof UserData uData){
            LoginUser loginUser = uData.getLoginUser(email);
            if (loginUser != null && passwordHashing.compare(password, loginUser.getPassword()))
                return userDAO.getById(loginUser.getUserID());
            return null;
        }
        else
            throw new Exception("Login Failed: This Login type is not supported");
    }
    public User create(User user, LoginUser loginUser) throws Exception {
        if (user.getName() == null || user.getName().isEmpty())
            throw new Exception("Name is required");
        if (user.getRole() == null)
            throw new Exception("Unknown role!");
        if (!EmailValidator.validate(user.getEmail()))
            throw new Exception("Email is invalid");
        if (loginUser.getPassword() == null || loginUser.getPassword().isEmpty())
            throw new Exception("Password not set");

        User newUser = userDAO.create(user);
        newUser.setImagePath(user.getImagePath());

        // gem login informationer
        if (userDAO instanceof UserData uData){
            LoginUser newLoginUser = new LoginUser(newUser.getId(), loginUser.getEmail(), passwordHashing.hashString(loginUser.getPassword()));
            uData.createLoginUser(newLoginUser);
        }

        return newUser;
    }

    public void update(User user) throws Exception {
        if (user.getRole() == null)
            throw new Exception("Role is required");
        userDAO.update(user);
    }
    public User getById(int id) throws Exception {
        return userDAO.getById(id);
    }
}

