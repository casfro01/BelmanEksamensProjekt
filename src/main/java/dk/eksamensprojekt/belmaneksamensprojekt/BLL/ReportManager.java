package dk.eksamensprojekt.belmaneksamensprojekt.BLL;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.*;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.Approved;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Image;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.OrderDaoFacade;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.ReportDAO;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.Repository;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;
import dk.eksamensprojekt.belmaneksamensprojekt.Main;

import java.io.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static dk.eksamensprojekt.belmaneksamensprojekt.BE.Report.REPORTS_PATH;

public class ReportManager {
    private static final String IMAGES_PATH = System.getProperty("user.dir") + File.separator + "Images" + File.separator;
    private final Repository<Order, String> orderRepository;
    private final Repository<Report, Integer> reportRepository;


    public ReportManager() {
        orderRepository = new OrderDaoFacade();
        reportRepository = new ReportDAO();
    }

    public void saveReport(Order order, List<String> comments) throws Exception {
        // todo : er der en anden måde at få user på, måske?
        Report report = new Report(-1, ModelManager.INSTANCE.getUserModel().getSelectedUser().get());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = generatePdfBytes(order, report, baos, comments);
        report.setReportBlob(bytes);
        // Report reportDatabase = orderRepository.create(report);
        order.setReport(report);
        orderRepository.update(order);
    }

    private static byte[] generatePdfBytes(Order order, Report report, ByteArrayOutputStream baos, List<String> comments) throws Exception {
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        Paragraph title = new Paragraph(
                "QC Report"
        ).setBold().setFontSize(30);
        title.setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        ImageData belmanLogo = ImageDataFactory.create(Objects.requireNonNull(Main.class.getResource("Icons/BELMAN_Logo.png")).getFile());
        document.add(new com.itextpdf.layout.element.Image(belmanLogo).scaleToFit(100F, 100F));

        Table infoTable = new Table(2);
        infoTable.setWidth(UnitValue.createPercentValue(100));
        infoTable.setMarginTop(20f);

        infoTable.addCell(createLabelCell("Order number:"));
        infoTable.addCell(createValueCell(order.getOrderNumber()));

        infoTable.addCell(createLabelCell("Written by:"));
        infoTable.addCell(createValueCell(ModelManager.INSTANCE.getUserModel().getSelectedUser().get().getName()));

        infoTable.addCell(createLabelCell("Contact:"));
        infoTable.addCell("belman@belman.com");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = LocalDate.now().format(formatter);
        infoTable.addCell(createLabelCell("Date:"));
        infoTable.addCell(createValueCell(formattedDate));

        document.add(infoTable);

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("\n"));

        int i = 0;
        for (Image image : order.getImageList()) {
            if (image.isApproved() == Approved.NOT_APPROVED) {
                continue;
            }
            
            Table table = new Table(new float[] {250F, 250F});
            table.setBorder(Border.NO_BORDER);
            table.setVerticalBorderSpacing(5);

            Cell cell1 = new Cell();
            cell1.setBorder(Border.NO_BORDER);
            cell1.setHorizontalAlignment(HorizontalAlignment.CENTER);
            ImageData data = ImageDataFactory.create("file:\\" + IMAGES_PATH + image.getPath());
            cell1.add(new com.itextpdf.layout.element.Image(data).scaleToFit(250F, 250F));

            Cell cell2 = new Cell();
            cell2.setBorder(Border.NO_BORDER);
            cell2.add(new Paragraph(comments.get(i)));

            if (i % 2 == 0) {
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

        String fontPath = String.valueOf(Main.class.getResource("Fonts/GreatVibes-Regular.ttf"));
        PdfFont font = PdfFontFactory.createFont(fontPath);
        Paragraph signature = new Paragraph(ModelManager.INSTANCE.getUserModel().getSelectedUser().get().getName())
                .setFont(font)
                .setFontSize(36);
        document.add(signature);
        document.close();

        return baos.toByteArray();
    }

    private static Cell createLabelCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setBold())
                .setBorder(Border.NO_BORDER)
                .setPaddingBottom(5f);
    }

    private static Cell createValueCell(String text) {
        return new Cell()
                .add(new Paragraph(text))
                .setBorder(Border.NO_BORDER)
                .setPaddingBottom(5f);
    }

    public Report getReport(Integer id) throws Exception {
        return reportRepository.getById(id);
    }

    public File downloadReport(Report report) throws IOException {
        //System.out.println(Arrays.toString(report.getReportBlob()));
        File outputFile = new File(REPORTS_PATH + report.getId() + ".pdf");
        FileOutputStream fos = new FileOutputStream(outputFile);
        fos.write(report.getReportBlob());
        return outputFile;
    }
}
