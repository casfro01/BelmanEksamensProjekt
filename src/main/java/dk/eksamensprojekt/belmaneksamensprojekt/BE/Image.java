package dk.eksamensprojekt.belmaneksamensprojekt.BE;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.Approved;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.ImagePosition;

// Java imports
import java.io.File;

/**
 * Billede klasse som hører til ordre klassen.
 * Denne klasse bruges til at holde styr på et enkelt billede tilhørende en ordre.
 * Klasse indeholder informationer om billede, såsom hvem der har taget den, stien til billedet etc.
 */
public class Image {
    //
    // Statiske konstanter
    //
    // TODO : refactor måske til en metode hvor man kan .getAbsolutePath() -> for at undgå for mange statiske importer ?
    // folder path
    public static final String IMAGES_PATH = System.getProperty("user.dir") + File.separator + "Images" + File.separator;

    //
    // Instans-variabler
    //
    private int id;
    private String path;
    private Approved isApproved;
    private User user;
    private int orderID;
    private ImagePosition imagePosition;

    //
    // Constructors
    //
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

    //
    // getters
    //
    public String getPath() {
        return path;
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

    //
    // setters
    //
    public void setImagePosition(ImagePosition imagePosition) { this.imagePosition = imagePosition; }
    public void setPath(String path) { this.path = path; }
    public void setApproved(Approved approved) { isApproved = approved; }
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
