package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model;

import dk.eksamensprojekt.belmaneksamensprojekt.BLL.LoginManager;

/**
 * Denne model skal bruges til at håndtere login (dog er dette ikke tilfældet - men det bliver det nok i senere versioner)
 */
public class LoginModel {
    private final LoginManager loginManager;

    public LoginModel() {
        loginManager = new LoginManager();
    }

    public boolean login(String email, String password) throws Exception {
        return loginManager.login(email, password);
    }
}
