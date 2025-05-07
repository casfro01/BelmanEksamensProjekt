package dk.eksamensprojekt.belmaneksamensprojekt.BLL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.LoginUser;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.util.IHashing;
import dk.eksamensprojekt.belmaneksamensprojekt.BLL.util.PasswordHasher;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.Repository;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.UserDAO;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.UserData;

import java.util.List;

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
        if (userDAO instanceof UserData uData){
            LoginUser loginUser = uData.getLoginUser(email);
            if (loginUser != null && passwordHashing.compare(password, loginUser.getPassword()))
                return userDAO.getById(loginUser.getUserID());
            return null;
        }
        else
            throw new Exception("Login Failed: This Login type is not supported");
    }
}

