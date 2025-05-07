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
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.OrderDaoFacade;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.ReportDAO;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.Repository;
import dk.eksamensprojekt.belmaneksamensprojekt.GUI.ModelManager;

import java.io.*;

import java.util.ArrayList;
import java.util.List;

public class ReportManager {
    private final Repository<Order, String> orderRepository;
    private final Repository<Report, Integer> reportRepository;


    public ReportManager() {
        orderRepository = new OrderDaoFacade();
        reportRepository = new ReportDAO();
    }

    public void saveReport(Order order, List<String> comments) throws Exception {
        Report report = new Report(-1, ModelManager.getInstance().getUserModel().getSelectedUser().get());

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

        return baos.toByteArray();
    }

    public Report getReport(Integer id) throws Exception {
        return reportRepository.getById(id);
    }
}
