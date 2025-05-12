package dk.eksamensprojekt.belmaneksamensprojekt.BE;


public class User {

    private int id;
    private String name;
    private String email;
    private UserRole role;

    public User(int id, int role, String email, String name) {
        this.role = UserRole.fromInt(role);
        this.email = email;
        this.name = name;
        this.id = id;
    }

    public User(int id, UserRole role, String email, String name) {
        this.role = role;
        this.email = email;
        this.name = name;
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }
}
