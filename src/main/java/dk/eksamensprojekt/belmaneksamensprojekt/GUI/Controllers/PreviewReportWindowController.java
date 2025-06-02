package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.*;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.Approved;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.UserActions;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Image;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.ReportModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.LogCreatorHelper;

// JavaFX
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

// Java
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

// Statiske imports
import static dk.eksamensprojekt.belmaneksamensprojekt.BE.Image.IMAGES_PATH;
import static dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts.splashMessage;

/**
 * Denne kontroller håndterer gennemgangen af en QC-Rapport
 */
public class PreviewReportWindowController extends Controller implements Initializable {
    private final static int COLUMNS = 2;
    private ModelManager modelManager;
    private OrderModel orderModel;
    private ReportModel reportModel;
    private List<TextArea> textAreas = new ArrayList<>();
    private List<Image> images = new ArrayList<>();
    private int row = 0;
    private int col = 0;

    //
    // JavaFX komponenter
    //
    @FXML
    private ScrollPane CheckboxscrollPane;
    @FXML
    private Label lblOrderNumber;
    @FXML
    private Label lblCustomer;
    @FXML
    private Label lblDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        modelManager = ModelManager.INSTANCE;
        orderModel = modelManager.getOrderModel();
        reportModel = modelManager.getReportModel();

        initializeScrollPane();
        initializeInformation();
    }

    /**
     * Klargøring af dét scrollPane som repræsenterer en rapport (med billeder og kommentar-felter).
     */
    private void initializeScrollPane() {
        // formatering
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(50);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.TOP_RIGHT);

        CheckboxscrollPane.setFitToWidth(true);
        CheckboxscrollPane.setContent(grid);

        grid.getChildren().clear();

        // løbe billederne igennem
        int i = 0;
        for (Image image : orderModel.getCurrentOrder().getImageList()) {
            if (image.isApproved() != Approved.NOT_APPROVED) {
                // lave "alternating pattern" for rapporten
                if (i % 2 == 0) {
                    addImage(grid, image);
                    addTextArea(grid);
                } else {
                    addTextArea(grid);
                    addImage(grid, image);
                }

                i += 1;
            }
        }

    }

    /**
     * Tilføj et billede
     */
    private void addImage(GridPane grid, Image image) {
        // indsætte billede
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(new javafx.scene.image.Image("file:\\" + IMAGES_PATH + image.getPath()));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(500);
        imageView.setFitHeight(300);

        StackPane imagePane = new StackPane(imageView/*, checkBox*/);
        imagePane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        images.add(image);

        // tilføj billede til rapport grid.
        grid.add(imagePane, col, row);
        col++;
        if (col >= COLUMNS) {
            col = 0;
            row++;
        }
    }

    /**
     * Tilføj et tekstfelt
     */
    private void addTextArea(GridPane grid) {
        // formatering + styling
        TextArea textArea = new TextArea();
        textArea.setPrefWidth(500);
        textArea.setPrefHeight(300);
        textArea.getStyleClass().add("textAreaFont");
        textAreas.add(textArea);

        // tilføj til grid
        grid.add(textArea, col, row);
        col++;
        if (col >= COLUMNS) {
            col = 0;
            row++;
        }
    }

    /**
     * Indsæt informationer omkring ordren (vises på vinduet ude i højre side).
     */
    private void initializeInformation() {
        lblOrderNumber.setText(orderModel.getCurrentOrder().getOrderNumber());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Order.DATE_FORMAT);
        lblDate.setText(LocalDate.now().format(dtf));
        lblCustomer.setText("??????????");
    }

    /**
     * Download rapporten (når man trykker på knappen)
     */
    @FXML
    private void downloadPressed(ActionEvent actionEvent) throws Exception {
        Report currentOrderReport = modelManager.getOrderModel().getCurrentOrder().getReport();
        if (currentOrderReport.getId() == 0) {
            return;
        }

        Report databaseReport = reportModel.getReport(currentOrderReport.getId());
        File outputFile = reportModel.downloadReport(databaseReport);
        Desktop.getDesktop().open(outputFile);
    }

    /**
     * Gemme rapporten i db / datakilden.
     */
    @FXML
    private void savePressed(ActionEvent actionEvent) throws Exception {
        List<String> strings = new ArrayList<>();
        for (TextArea textArea : textAreas) {
            if (!textArea.isDisabled()) {
                strings.add(textArea.getText());
            }
        }
        for (Image image : images) {
            if (image.isApproved() == Approved.NOT_APPROVED){
                image.setOrderId(-1); // sætter id til -1 for så kan man håndtere det i data laget
                // kan ikke fjerne image fra listen her, fordi så vil billederne ikke blive opdateret i DAO senere
            } else {
                image.setApproved(Approved.APPROVED);
                image.setOrderId(orderModel.getCurrentOrder().getId());
            }
        }
        // gemmer rapport
        reportModel.saveReport(strings);
        ModelManager.INSTANCE.getLogModel().createLog(LogCreatorHelper.logFactory(UserActions.CREATE_REPORT));
        splashMessage("Report saved", "The report has been saved!" , 1.5);
    }
}
