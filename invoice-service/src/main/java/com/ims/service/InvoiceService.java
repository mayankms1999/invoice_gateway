package com.ims.service;

import com.ims.dto.InvoiceRequest;
import com.ims.dto.InvoiceResponse;
import com.ims.exception.InvoiceNotFoundException;
import com.ims.model.Invoice;
import com.ims.model.InvoiceItem;
import com.ims.model.UserDetail;
import com.ims.repository.InvoiceRepository;
import com.ims.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;

    @Transactional
    public InvoiceResponse createInvoice(InvoiceRequest request) {
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
                .createdAt(LocalDateTime.now())
                .items(items)
                .userDetail(user)
                .build();

        items.forEach(item -> item.setInvoice(invoice)); // Set invoice reference in items

        invoiceRepository.save(invoice);
        log.info("Invoice created successfully: {}", invoice.getInvoiceNumber());

        return mapToResponse(invoice);
    }

    public InvoiceResponse getInvoiceById(Long id) {
        log.info("Fetching invoice with ID: {}", id);
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Invoice not found with ID: {}", id);
                    return new InvoiceNotFoundException("Invoice not found with ID: " + id);
                });

        return mapToResponse(invoice);
    }

    public List<InvoiceResponse> getAllInvoices() {
        log.info("Fetching all invoices...");
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoices.stream().map(this::mapToResponse).collect(Collectors.toList());
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
}
