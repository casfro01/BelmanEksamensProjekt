package dk.eksamensprojekt.belmaneksamensprojekt.BE;


public class User {

    private int id;
    private String name;
    private String email;
    private int role;


    public User(){
        id = 0;
        name = "";
        role = 0;
    }

    public User(int id, int role, String email, String name) {
        this.role = role;
        this.email = email;
        this.name = name;
        this.id = id;
    }
}
