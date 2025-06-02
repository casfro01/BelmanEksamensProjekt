package dk.eksamensprojekt.belmaneksamensprojekt.BE;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.UserRole;

/**
 * Denne bruger klasse er en udvidelse af BaseUser med flere informationer så som bruger-rolle, profilbillede og email.
 */
public class User extends BaseUser{
    //
    // Instans-variabler
    //
    private String email;
    private UserRole role;
    private String ImagePath;

    //
    // Constructors
    //
    public User(int id, int role, String email, String name) {
        super(id, name);
        this.role = UserRole.fromInt(role);
        this.email = email;
        this.ImagePath = getBasicUserImage();
    }

    public User(int id, UserRole role, String email, String name) {
        super(id, name);
        this.role = role;
        this.email = email;
        this.ImagePath = getBasicUserImage();
    }

    //
    // Getters
    //
    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public String getImagePath() { return ImagePath;}
    public static String getBasicUserImage(){ return "Icons/BasicUser.jpg"; }

    //
    // Setters
    //
    public void setImagePath(String imagePath) {ImagePath = imagePath;}
    public void setRole(UserRole role) { this.role = role; }
}
