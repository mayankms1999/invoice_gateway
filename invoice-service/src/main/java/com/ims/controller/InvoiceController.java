package com.ims.controller;

import com.ims.dto.InvoiceRequest;
import com.ims.dto.InvoiceResponse;
import com.ims.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
@Slf4j
public class InvoiceController {
    private final InvoiceService invoiceService;

    // POST: Create a new invoice
    @PostMapping("/create")
    public ResponseEntity<InvoiceResponse> createInvoice(@Valid @RequestBody InvoiceRequest request) {
        log.info("Received request to create an invoice: {}", request);
        InvoiceResponse response = invoiceService.createInvoice(request);
        log.info("Invoice created successfully: {}", response);
        return ResponseEntity.status(201).body(response);
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
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        log.info("Fetching all invoices...");
        List<InvoiceResponse> invoices = invoiceService.getAllInvoices();
        if (invoices.isEmpty()) {
            log.warn("No invoices found in the database.");
            return ResponseEntity.noContent().build();
        }
        log.info("Returning {} invoices", invoices.size());
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
