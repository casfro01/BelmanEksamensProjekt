package dk.eksamensprojekt.belmaneksamensprojekt.BE;

public class Image {
    private int id;
    private String path;
    private boolean isApproved;

    public Image(int id, String path, boolean isApproved) {
        this.id = id;
        this.path = path;
        this.isApproved = isApproved;
    }

    private String getPath() {
        return path;
    }

    private void setPath(String path) {
        this.path = path;
    }

    private boolean isApproved() {
        return isApproved;
    }

    private void setApproved(boolean approved) {
        isApproved = approved;
    }
}
