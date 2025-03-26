package com.ims.controller;

import com.ims.dto.InvoiceRequest;
import com.ims.model.Product;
import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.io.ByteArrayOutputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/pdf")
public class GeneratePdf {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/generate-invoice")
    public ResponseEntity<ByteArrayResource> generateInvoice(@RequestBody InvoiceRequest request) throws IOException, MessagingException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        Paragraph title = new Paragraph("Invoice").setBold().setFontSize(20);
        document.add(title);

        Table headerTable = new Table(new float[]{50, 50});
        headerTable.setWidth(UnitValue.createPercentValue(100));
        headerTable.addCell(new Cell().add(new Paragraph("Invoice No.: " + request.getInvoiceNumber()).setBold()).setBorder(Border.NO_BORDER));
        headerTable.addCell(new Cell().add(new Paragraph("Invoice Date: " + request.getInvoiceDate()).setBold()).setBorder(Border.NO_BORDER));
        document.add(headerTable);
        document.add(new Paragraph("\n"));

        Table infoTable = new Table(new float[]{50, 50});
        infoTable.setWidth(UnitValue.createPercentValue(100));
        infoTable.addCell(new Cell().add(new Paragraph("Billing Info").setBold()).setBorder(Border.NO_BORDER));
        infoTable.addCell(new Cell().add(new Paragraph("Shipping Info").setBold()).setBorder(Border.NO_BORDER));
        infoTable.addCell(new Cell().add(new Paragraph("Company: ABC Corp")).setBorder(Border.NO_BORDER));
        infoTable.addCell(new Cell().add(new Paragraph("Name: " + request.getShippingName())).setBorder(Border.NO_BORDER));
        infoTable.addCell(new Cell().add(new Paragraph("Address: 123 Main Street")).setBorder(Border.NO_BORDER));
        infoTable.addCell(new Cell().add(new Paragraph("Address: " + request.getShippingAddress())).setBorder(Border.NO_BORDER));
        infoTable.addCell(new Cell().add(new Paragraph("Email: abc@corp.com")).setBorder(Border.NO_BORDER));
        infoTable.addCell(new Cell().add(new Paragraph("Email: " + request.getShippingEmail())).setBorder(Border.NO_BORDER));
        document.add(infoTable);
        document.add(new Paragraph("\n"));

        Table itemTable = new Table(new float[]{60, 20, 20});
        itemTable.setWidth(UnitValue.createPercentValue(100));
        itemTable.addHeaderCell(new Cell().add(new Paragraph("Description").setBold()));
        itemTable.addHeaderCell(new Cell().add(new Paragraph("Quantity").setBold()));
        itemTable.addHeaderCell(new Cell().add(new Paragraph("Price").setBold()));
        double total = 0;
        List<Product> products = request.getProducts();
        for (Product product : products) {
            itemTable.addCell(new Cell().add(new Paragraph(product.getDescription())));
            itemTable.addCell(new Cell().add(new Paragraph(String.valueOf(product.getQuantity()))));
            itemTable.addCell(new Cell().add(new Paragraph(String.valueOf(product.getPrice()))));
            total += product.getQuantity() * product.getPrice();
        }
        document.add(itemTable);

        Table totalTable = new Table(new float[]{50, 50});
        totalTable.setWidth(UnitValue.createPercentValue(100));
        totalTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        totalTable.addCell(new Cell().add(new Paragraph("Total: $" + total).setBold()).setBorder(Border.NO_BORDER));
        document.add(totalTable);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("TERMS AND CONDITIONS:").setBold().setFontSize(12));
        document.add(new Paragraph("1. The Seller shall not be liable to the Buyer directly or indirectly for any loss or damage suffered by the Buyer.").setFontSize(10));
        document.close();

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        sendEmail(request.getShippingEmail(), outputStream.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf");

        return ResponseEntity.ok().headers(headers).contentLength(resource.contentLength()).contentType(MediaType.APPLICATION_PDF).body(resource);
    }

    private void sendEmail(String to, byte[] pdfBytes) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Invoice");
        helper.setText("Please find the attached invoice.");
        helper.addAttachment("invoice.pdf", new ByteArrayResource(pdfBytes));
        mailSender.send(message);
        System.out.println("Email sent to: " + to);
    }
}
