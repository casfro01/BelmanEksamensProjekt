package dk.eksamensprojekt.belmaneksamensprojekt.BE;

public class Image {
    private int id;
    private String path;
    private boolean isApproved;
    private User user;
    private int orderID;

    public Image(int id, String path, boolean isApproved) {
        this.id = id;
        this.path = path;
        this.isApproved = isApproved;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
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
