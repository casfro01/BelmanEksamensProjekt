package dk.eksamensprojekt.belmaneksamensprojekt.BE;

/**
 * Base(simpel) user(bruger) login klasse, som kun har den essentielle informationer om en brugers login informationer, såsom email -
 * som bruges til at identificerer brugeren (når id'et sandsynligvis ikke kendes) og brugerens password -
 * (enten hashed eller ik' -> baseret på hvilken retning den rejser).
 *
 * Lige nu kan man godt konverterer klassen til en record-class nu nåt alt er final.
 */
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
