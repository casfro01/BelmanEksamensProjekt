package dk.eksamensprojekt.belmaneksamensprojekt.BE;

public class LoginUser{
    private final String email;
    private final String password;
    private final int userID;

    public LoginUser(int userID, String email, String password) {
        this.userID = userID;
        this.email = email;
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public int getUserID() {
        return userID;
    }
}
