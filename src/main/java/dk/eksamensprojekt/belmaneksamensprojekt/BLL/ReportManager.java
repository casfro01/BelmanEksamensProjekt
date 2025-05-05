package dk.eksamensprojekt.belmaneksamensprojekt.BLL;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.*;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.ReportDAO;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

public class ReportManager {
    private static final String IMAGES_PATH = System.getProperty("user.dir") + File.separator + "Images" + File.separator;
    private static final String REPORTS_PATH = System.getProperty("user.dir") + File.separator + "Reports" + File.separator;

    private final ReportDAO reportDAO;

    public ReportManager() {
        reportDAO = new ReportDAO();
    }

    public void saveReport(Order order, List<String> comments) throws Exception {
        Report report = new Report(-1, "/Reports/", ModelManager.getInstance().getUserModel().getSelectedUser().get());
        Report reportDatabase = reportDAO.create(report);
        order.setReport(reportDatabase);

        // [TODO] update order i database så den nu linker til den nye report?

        generatePdf(order, reportDatabase, comments);
    }

    private static void generatePdf(Order order, Report report, List<String> comments) throws Exception {
        String pdfPath = REPORTS_PATH + report.getId() + ".pdf";
        PdfWriter writer = new PdfWriter(pdfPath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        Paragraph title = new Paragraph(
                "QC Report"
        ).setBold().setFontSize(30);
        title.setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        int i = 0;
        for (Image image : order.getImageList()) {
            Table table = new Table(new float[] {250F, 250F});
            table.setBorder(Border.NO_BORDER);
            table.setVerticalBorderSpacing(5);

            Cell cell1 = new Cell();
            cell1.setBorder(Border.NO_BORDER);
            cell1.setHorizontalAlignment(HorizontalAlignment.CENTER);
            ImageData data = ImageDataFactory.create(image.getPath());
            cell1.add(new com.itextpdf.layout.element.Image(data).scaleToFit(250F, 250F));

            Cell cell2 = new Cell();
            cell2.setBorder(Border.NO_BORDER);
            cell2.add(new Paragraph(comments.get(i)));

            if (i % 2 == 1) {
                table.addCell(cell1);
                table.addCell(cell2);
            } else {
                table.addCell(cell2);
                table.addCell(cell1);
            }

            table.setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(table);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));

            i += 1;
        }

        document.close();
    }

}
