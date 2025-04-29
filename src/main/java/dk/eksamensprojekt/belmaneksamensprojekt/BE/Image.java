package dk.eksamensprojekt.belmaneksamensprojekt.BE;

public class Image {
    private int id;
    private String path;
    private Approved isApproved;
    private User user;
    private int orderID;

    public Image(int id, String path, Approved isApproved) {
        this.id = id;
        this.path = path;
        this.isApproved = isApproved;
    }

    public Image(int id, String path, Approved isApproved, User user, int orderID) {
        this.id = id;
        this.path = path;
        this.isApproved = isApproved;
        this.user = user;
        this.orderID = orderID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Approved isApproved() {
        return isApproved;
    }

    public void setApproved(Approved approved) {
        isApproved = approved;
    }
    public int getId() {
        return id;
    }
    public User getUser(){
        return user;
    }
    public int getOrderID() {
        return orderID;
    }
}
