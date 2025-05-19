package dk.eksamensprojekt.belmaneksamensprojekt.BE;

public class Image {
    private int id;
    private String path;
    private Approved isApproved;
    private User user;
    private int orderID;
    private ImagePosition imagePosition;

    public Image(int id, String path, Approved isApproved) {
        this.id = id;
        this.path = path;
        this.isApproved = isApproved;
        this.imagePosition = ImagePosition.EXTRA;
        this.orderID = 0;
        this.user = null;
    }

    public Image(int id, String path, Approved isApproved, User user, int orderID) {
        this.id = id;
        this.path = path;
        this.isApproved = isApproved;
        this.user = user;
        this.orderID = orderID;
        this.imagePosition = ImagePosition.EXTRA;
    }

    public Image(int id, String path, Approved isApproved, User user, int orderID, ImagePosition imagePosition) {
        this.id = id;
        this.path = path;
        this.isApproved = isApproved;
        this.user = user;
        this.orderID = orderID;
        this.imagePosition = imagePosition;
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

    public int getId() {
        return id;
    }
    public User getUser(){
        return user;
    }
    public int getOrderID() {
        return orderID;
    }
    public ImagePosition getImagePosition() { return imagePosition; }

    public void setImagePosition(ImagePosition imagePosition) { this.imagePosition = imagePosition; }
    public void setApproved(Approved approved) {
        isApproved = approved;
    }
    public void setOrderId(int id) { this.orderID = id; }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", isApproved=" + isApproved +
                ", user=" + user +
                ", orderID=" + orderID +
                ", imagePosition=" + imagePosition +
                '}';
    }
}
