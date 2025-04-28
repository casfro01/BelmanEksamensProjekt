package dk.eksamensprojekt.belmaneksamensprojekt.BE;

public class LoginUser extends User{
    private final String email;
    private final String password;

    public LoginUser(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}
