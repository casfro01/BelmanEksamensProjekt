module dk.eksamensprojekt.belmaneksamensprojekt {
    requires javafx.controls;
    requires javafx.fxml;


    opens dk.eksamensprojekt.belmaneksamensprojekt to javafx.fxml;
    exports dk.eksamensprojekt.belmaneksamensprojekt;
}