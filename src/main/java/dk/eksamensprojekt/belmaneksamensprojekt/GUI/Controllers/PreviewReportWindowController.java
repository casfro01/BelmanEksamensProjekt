package dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controllers;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.*;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Image;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Controller;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.OrderModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.Model.ReportModel;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.util.ShowAlerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static dk.eksamensprojekt.belmaneksamensprojekt.Constants.Constants.IMAGES_PATH;

public class PreviewReportWindowController extends Controller implements Initializable {
    private final static int COLUMNS = 2;
    private ModelManager modelManager;
    private OrderModel orderModel;
    private ReportModel reportModel;
    private List<TextArea> textAreas = new ArrayList<>();
    private List<Image> images = new ArrayList<>();
    //private List<CheckBox> checkboxes = new ArrayList<>();
    private int row = 0;
    private int col = 0;

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

    private void initializeScrollPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(50);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.TOP_RIGHT);

        CheckboxscrollPane.setFitToWidth(true);
        CheckboxscrollPane.setContent(grid);

        grid.getChildren().clear();

        int i = 0;
        for (Image image : orderModel.getCurrentOrder().getImageList()) {
            if (image.isApproved() != Approved.NOT_APPROVED) {
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

    private void addImage(GridPane grid, Image image) {
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(new javafx.scene.image.Image("file:\\" + IMAGES_PATH + image.getPath()));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(500);
        imageView.setFitHeight(300);

//        CheckBox checkBox = new CheckBox();
//        checkBox.setStyle("-fx-padding: 10px");
//        checkBox.setSelected(true);
//        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
//            textAreas.get(checkboxes.indexOf(checkBox)).setDisable(!newValue);
//        });

        StackPane imagePane = new StackPane(imageView/*, checkBox*/);
        imagePane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
//        StackPane.setAlignment(checkBox, Pos.TOP_RIGHT);
//        StackPane.setMargin(checkBox, new Insets(10));
//
//        checkboxes.add(checkBox);
        images.add(image);

        grid.add(imagePane, col, row);
        col++;
        if (col >= COLUMNS) {
            col = 0;
            row++;
        }
    }

    private void addTextArea(GridPane grid) {
        TextArea textArea = new TextArea();
        textArea.setPrefWidth(500);
        textArea.setPrefHeight(300);
        textArea.getStyleClass().add("textAreaFont");
        textAreas.add(textArea);
        grid.add(textArea, col, row);
        col++;
        if (col >= COLUMNS) {
            col = 0;
            row++;
        }
    }

    private void initializeInformation() {
        lblOrderNumber.setText(orderModel.getCurrentOrder().getOrderNumber());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lblDate.setText(LocalDate.now().format(dtf));
        lblCustomer.setText("??????????");
    }

    public void downloadPressed(ActionEvent actionEvent) throws Exception {
        Report currentOrderReport = modelManager.getOrderModel().getCurrentOrder().getReport();
        if (currentOrderReport.getId() == 0) {
            return;
        }

        Report databaseReport = reportModel.getReport(currentOrderReport.getId());
        orderModel.downloadReport(databaseReport);
    }

    public void savePressed(ActionEvent actionEvent) throws Exception {
        List<String> strings = new ArrayList<>();
        for (TextArea textArea : textAreas) {
            if (!textArea.isDisabled()) {
                strings.add(textArea.getText());
            }
        }
        for (Image image : images) {
            //if (!checkboxes.get(images.indexOf(image)).isSelected()) {
            if (image.isApproved() == Approved.NOT_APPROVED){
                //image.setApproved(Approved.NOT_APPROVED);
                image.setOrderId(-1);
                // orderModel.getCurrentOrder().getImageList().remove(image);
                // kan ikke fjerne image fra listen her, fordi så vil billederne ikke blive opdateret i DAO senere
            } else {
                image.setApproved(Approved.APPROVED);
                image.setOrderId(orderModel.getCurrentOrder().getId());
            }
        }
        reportModel.saveReport(strings);
        ShowAlerts.splashMessage("Report saved", "The report has been saved!" , 1.5);
    }
}
