package com.ims.service;

import com.ims.dto.InvoiceRequest;
import com.ims.dto.InvoiceResponse;
import com.ims.exception.InvoiceNotFoundException;
import com.ims.model.Invoice;
import com.ims.model.InvoiceItem;
import com.ims.model.UserDetail;
import com.ims.repository.InvoiceRepository;
import com.ims.repository.UserRepository;
import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;


@Transactional
public ResponseEntity<ByteArrayResource> createInvoice(InvoiceRequest request) throws MessagingException {
    log.info("Creating invoice for user: {}", request.getUser().getEmail());

    // Create User
    UserDetail user = UserDetail.builder()
            .name(request.getUser().getName())
            .email(request.getUser().getEmail())
            .phone(request.getUser().getPhone())
            .build();

    List<InvoiceItem> items = request.getProducts().stream()
            .map(product -> InvoiceItem.builder()
                    .productId(product.getProductId())
                    .productName(product.getProductName())
                    .quantity(product.getQuantity())
                    .price(product.getPrice())
                    .build())
            .collect(Collectors.toList());

    // Create Invoice
    Invoice invoice = Invoice.builder()
            .invoiceNumber(UUID.randomUUID().toString())
            .createdAt(Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)).toLocalDateTime())
            .items(items)
            .userDetail(user)
            .build();

    items.forEach(item -> item.setInvoice(invoice)); // Set invoice reference in items

//    invoiceRepository.save(invoice);
    log.info("Invoice created successfully: {}", invoice.getInvoiceNumber());

    return generateInvoicePdf(invoice, request);

}

    public InvoiceResponse getInvoiceById(Long id) {
        log.info("Fetching invoice with ID: {}", id);
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Invoice not found with ID: {}", id);
                    return new InvoiceNotFoundException("Invoice not found with ID: " + id);
                });
        System.out.println("user set seccessfully");
        return mapToResponse(invoice);
    }

    public Page<InvoiceResponse> getAllInvoices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Invoice> invoice = invoiceRepository.findAll(pageable);
        return invoice.map(invoice1 -> new InvoiceResponse(
                invoice1.getId(),
                invoice1.getInvoiceNumber(),
                invoice1.getCreatedAt(),
                invoice1.getItems(),
                invoice1.getUserDetail()
        ));
    }

    @Transactional
    public void deleteInvoiceItem(Long itemId) {
        log.info("Attempting to delete invoice item with ID: {}", itemId);

        InvoiceItem item = invoiceRepository.findInvoiceItemById(itemId)
                .orElseThrow(() -> {
                    log.error("Invoice item not found with ID: {}", itemId);
                    return new InvoiceNotFoundException("Invoice item not found with ID: " + itemId);
                });

        invoiceRepository.deleteInvoiceItemById(itemId);
        log.info("Successfully deleted invoice item with ID: {}", itemId);
    }

    private InvoiceResponse mapToResponse(Invoice invoice) {
        return new InvoiceResponse(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getCreatedAt(),
                invoice.getItems(),
                invoice.getUserDetail()
        );
    }


    public ResponseEntity<ByteArrayResource> generateInvoicePdf(Invoice invoice, InvoiceRequest request) throws IOException, MessagingException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        Paragraph title = new Paragraph("Invoice").setBold().setFontSize(20);
        document.add(title);

        // Format date and time before adding to PDF
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String formattedDate = invoice.getCreatedAt().format(dateFormatter);
        String formattedTime = invoice.getCreatedAt().format(timeFormatter);

        Table headerTable = new Table(new float[]{50, 50});
        headerTable.setWidth(UnitValue.createPercentValue(100));
        headerTable.addCell(new Cell().add(new Paragraph("Invoice No.: ").setBold()).setBorder(Border.NO_BORDER));
        headerTable.addCell(new Cell().add(new Paragraph("Invoice Date: ").setBold()).setBorder(Border.NO_BORDER));
        headerTable.addCell(new Cell().add(new Paragraph( invoice.getInvoiceNumber()).setBold()).setBorder(Border.NO_BORDER));
        headerTable.addCell(new Cell().add(new Paragraph(formattedDate + " " + formattedTime).setBold()).setBorder(Border.NO_BORDER));
        document.add(headerTable);
        document.add(new Paragraph("\n"));

        Table infoTable = new Table(new float[]{50, 50});
        infoTable.setWidth(UnitValue.createPercentValue(100));
        infoTable.addCell(new Cell().add(new Paragraph("Billing Info").setBold()).setBorder(Border.NO_BORDER));
        infoTable.addCell(new Cell().add(new Paragraph("Customer Info").setBold()).setBorder(Border.NO_BORDER));
        infoTable.addCell(new Cell().add(new Paragraph("Company: System Electonics Pvt.Ltd")).setBorder(Border.NO_BORDER));
        infoTable.addCell(new Cell().add(new Paragraph("Name: " + invoice.getUserDetail().getName())).setBorder(Border.NO_BORDER));
        infoTable.addCell(new Cell().add(new Paragraph("Address: Noida sector 63")).setBorder(Border.NO_BORDER));
        infoTable.addCell(new Cell().add(new Paragraph("Contact: " + invoice.getUserDetail().getPhone())).setBorder(Border.NO_BORDER));
        infoTable.addCell(new Cell().add(new Paragraph("Email: system@electonics.com")).setBorder(Border.NO_BORDER));
        infoTable.addCell(new Cell().add(new Paragraph("Email: " + invoice.getUserDetail().getEmail())).setBorder(Border.NO_BORDER));
        document.add(infoTable);
        document.add(new Paragraph("\n"));

        Table itemTable = new Table(new float[]{60, 20, 20});
        itemTable.setWidth(UnitValue.createPercentValue(100));
        itemTable.addHeaderCell(new Cell().add(new Paragraph("Description").setBold()));
        itemTable.addHeaderCell(new Cell().add(new Paragraph("Quantity").setBold()));
        itemTable.addHeaderCell(new Cell().add(new Paragraph("Total").setBold()));
        double grandTotal = 0;
        for (InvoiceItem item : invoice.getItems()) {
            itemTable.addCell(new Cell().add(new Paragraph(item.getProductName())));
            itemTable.addCell(new Cell().add(new Paragraph(String.valueOf(item.getQuantity()))));
            itemTable.addCell(new Cell().add(new Paragraph(String.valueOf(item.getPrice()))));
            grandTotal += item.getQuantity() * item.getPrice();
        }

        document.add(itemTable);

        Table totalTable = new Table(new float[]{50, 50});
        totalTable.setWidth(UnitValue.createPercentValue(100));
        totalTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        totalTable.addCell(new Cell().add(new Paragraph("Grand Total: ₹" + grandTotal).setBold()).setBorder(Border.NO_BORDER));
        document.add(totalTable);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("TERMS AND CONDITIONS:").setBold().setFontSize(12));
        document.add(new Paragraph("1. The Seller shall not be liable to the Buyer directly or indirectly for any loss or damage suffered by the Buyer.").setFontSize(10));
        document.close();

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        sendEmail(invoice.getUserDetail().getEmail(), outputStream.toByteArray());
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
