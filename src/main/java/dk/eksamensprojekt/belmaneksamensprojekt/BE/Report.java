package dk.eksamensprojekt.belmaneksamensprojekt.BE;

import java.util.List;

public class Report {
    private List<Image> images;

    public Report(List<Image> images) {
        this.images = images;
    }

    public List<Image> getImages() {
        return images;
    }
}
