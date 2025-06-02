package dk.eksamensprojekt.belmaneksamensprojekt.BLL;

import dk.eksamensprojekt.belmaneksamensprojekt.BLL.util.PasswordHasher;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.UserDAO;

/**
 * Manager til at håndtere login
 */
public class LoginManager {
    private final PasswordHasher hasher;
    private final UserDAO userDAO;

    public LoginManager() {
        hasher = new PasswordHasher();
        userDAO = new UserDAO();
    }

    public boolean login(String email, String password) throws Exception {
        String databasePassword = userDAO.getLoginUser(email).getPassword();

        return hasher.compare(password, databasePassword);
    }
}
