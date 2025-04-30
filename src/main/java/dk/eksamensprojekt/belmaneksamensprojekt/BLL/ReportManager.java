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
    private final ModelManager modelManager;

    public ReportManager() {
        reportDAO = new ReportDAO();
        modelManager = ModelManager.getInstance();
    }

    public void saveReport(Order order, List<String> comments) throws Exception {
        Report report = new Report(-1, "/Reports/", modelManager.getUserModel().getSelectedUser().get());
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
            cell1.add(new com.itextpdf.layout.element.Image(data));

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

        // [TODO] loop igennem images og tilføj til pdf, men inden det tilføj en string fra comments

        document.close();
    }

    public static void main(String[] args) throws Exception {
        User user = new User(1, 1, "email", "name");
        Report report = new Report(1, REPORTS_PATH + "sdadj.pdf", user);
        Order order = new Order(-1, "1231", report, Approved.NotReviewed);
        order.getImageList().add(new Image(1, IMAGES_PATH + "image1.jpg", Approved.NotApproved));
        order.getImageList().add(new Image(2, IMAGES_PATH + "image2.png", Approved.NotApproved));
        generatePdf(order, report, new ArrayList<String>() {{
            add("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec pharetra, elit sed feugiat varius, diam leo eleifend neque, eget blandit elit nisi ac justo. Suspendisse sollicitudin sapien quis est tempus, sed euismod arcu vehicula. Duis ac quam placerat, ornare erat ac, volutpat velit. Fusce pulvinar nisi elit, nec rutrum sapien facilisis eu. Suspendisse a mauris fringilla, aliquam tortor eget, finibus sem. In hac habitasse platea dictumst.");
            add("Nunc ac elit vitae ligula placerat molestie id quis magna. Donec ultricies, ligula et tempor placerat, dolor mi imperdiet purus, quis placerat nibh massa facilisis risus. Proin accumsan placerat erat quis venenatis. Nam lacinia metus eu enim maximus, in consequat lectus ornare. In dictum quam quis ex mollis, non auctor libero vehicula. Phasellus a mollis lectus. Maecenas venenatis nibh ut sem blandit molestie.");
        }});
    }
}
