package dk.eksamensprojekt.belmaneksamensprojekt.BLL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.User;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.UserDAO;

import java.util.List;

public class UserManager {
    private final UserDAO userDAO;

    public UserManager() {
        this.userDAO = new UserDAO();
    }

    public List<User> getAllUsers() throws Exception {
        return userDAO.getAll();
    }
}

