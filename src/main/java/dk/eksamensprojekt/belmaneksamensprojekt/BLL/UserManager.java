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
    private static final String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final Pattern pattern = Pattern.compile(emailRegex);

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
    public User create(User user) throws Exception {
        if (user.getName() == null || user.getName().isEmpty())
            throw new Exception("Name is required");
        if (pattern.matcher(user.getEmail()).matches())
            throw new Exception("Email is invalid");
        //if (user.getPassword() == null || user.getPassword().length() < 6)
        //        throw new Exception("Password must be at least 6 characters");
        return userDAO.create(user);
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

