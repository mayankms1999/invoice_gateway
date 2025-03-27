package com.ims.controller;

import com.ims.dto.InvoiceRequest;
import com.ims.dto.InvoiceResponse;
import com.ims.service.InvoiceService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
@Slf4j
public class InvoiceController {
    private final InvoiceService invoiceService;
    @PostMapping("/create")
    public ResponseEntity<ByteArrayResource> createInvoice(@RequestBody InvoiceRequest request) throws IOException, MessagingException {
        return invoiceService.createInvoice(request);
    }

    // GET: Retrieve an invoice by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<InvoiceResponse> getInvoice(@PathVariable("id") Long id) {
        log.info("Fetching invoice with ID: {}", id);
        InvoiceResponse response = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(response);
    }

    // GET: Retrieve all invoices
    @GetMapping("/get-all")
    public ResponseEntity<Page<InvoiceResponse>> getAllInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        log.info("Fetching invoices - Page: {}, Size: {}", page, size);

        Page<InvoiceResponse> invoices = invoiceService.getAllInvoices(page, size);

        if (invoices.isEmpty()) {
            log.warn("No invoices found in the database.");
            return ResponseEntity.noContent().build();
        }

        log.info("Returning {} invoices", invoices.getTotalElements());
        return ResponseEntity.ok(invoices);
    }

    // DELETE: Remove an invoice item by ID
    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<Void> deleteInvoiceItem(@PathVariable Long itemId) {
        log.info("Deleting invoice item with ID: {}", itemId);
        invoiceService.deleteInvoiceItem(itemId);
        log.info("Invoice item with ID {} deleted successfully", itemId);
        return ResponseEntity.noContent().build();
    }
}
