package com.oceanview.helper;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

//import com.itextpdf.layout.property.TextAlignment;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PdfGeneratorHelper {

    public static byte[] generateReservationPdf(
            String reservationNumber,
            String guestName,
            Integer roomNumber,
            String checkIn,
            String checkOut,
            BigDecimal totalBill
    ) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);

        PageSize pageSize = new PageSize(320, 420);

        Document document = new Document(pdf, pageSize);
        document.setMargins(20, 20, 20, 20);

        // Hotel Name
        document.add(new Paragraph("Ocean View Resort")
                .setBold()
                .setFontSize(16));
//                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("Reservation Invoice"));
//                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Reservation No: " + reservationNumber));
        document.add(new Paragraph("Date: " + LocalDate.now()));

        document.add(new Paragraph("-------------------------------"));

        // Table Layout
        Table table = new Table(2);

        table.addCell("Guest");
        table.addCell(guestName);

        table.addCell("Room");
        table.addCell(String.valueOf(roomNumber));

        table.addCell("Check In");
        table.addCell(checkIn);

        table.addCell("Check Out");
        table.addCell(checkOut);

        table.addCell("Total Bill");
        table.addCell("LKR " + totalBill);

        document.add(table);

        document.add(new Paragraph("\n-------------------------------"));

        document.add(new Paragraph("Thank you for staying with us!"));
//                .setTextAlignment(TextAlignment.CENTER));

        document.close();

        return out.toByteArray();
    }
}