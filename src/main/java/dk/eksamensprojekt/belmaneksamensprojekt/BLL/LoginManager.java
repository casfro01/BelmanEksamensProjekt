package dk.eksamensprojekt.belmaneksamensprojekt.BLL;

import dk.eksamensprojekt.belmaneksamensprojekt.BLL.util.PasswordHasher;

public class LoginManager {
    private final PasswordHasher hasher;

    public LoginManager() {
        hasher = new PasswordHasher();
    }
    public boolean login(String email, String password) throws Exception {
        String databasePassword = "test"; // get from user dao

        if (hasher.compare(password, databasePassword)) {
            return true;
        } else {
            return false;
        }
    }
}
