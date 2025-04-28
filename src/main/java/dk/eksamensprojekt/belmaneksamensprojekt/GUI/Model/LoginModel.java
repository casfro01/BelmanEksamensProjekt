package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model;

import dk.eksamensprojekt.belmaneksamensprojekt.BLL.LoginManager;

public class LoginModel {
    private final LoginManager loginManager;

    public LoginModel() {
        loginManager = new LoginManager();
    }

    public boolean login(String email, String password) throws Exception {
        return loginManager.login(email, password);
    }
}
