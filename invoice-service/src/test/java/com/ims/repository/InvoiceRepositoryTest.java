package com.ims.repository;

import com.ims.model.Invoice;
import com.ims.model.InvoiceItem;
import com.ims.model.UserDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class InvoiceRepositoryTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Invoice savedInvoice;
    private InvoiceItem savedInvoiceItem;

    @BeforeEach
    void setUp() {
        // Create a UserDetail
        UserDetail userDetail = UserDetail.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();
        userDetail = entityManager.persistFlushFind(userDetail);

        // Create an InvoiceItem
        InvoiceItem invoiceItem = InvoiceItem.builder()
                .productId(1L)
                .productName("Laptop")
                .quantity(2)
                .price(1500.00)
                .build();

        // Create an Invoice (with at least one item)
        Invoice invoice = Invoice.builder()
                .invoiceNumber("INV-1001")
                .createdAt(LocalDateTime.now())
                .items(List.of(invoiceItem))
                .userDetail(userDetail)
                .build();

        // Set the Invoice reference in InvoiceItem
        invoiceItem.setInvoice(invoice);

        // Persist the Invoice (which also persists InvoiceItem)
        savedInvoice = entityManager.persistFlushFind(invoice);
        savedInvoiceItem = savedInvoice.getItems().get(0);
    }


    @Test
    void testFindInvoiceItemById() {
        Optional<InvoiceItem> foundItem = invoiceRepository.findInvoiceItemById(savedInvoiceItem.getId());

        assertThat(foundItem).isPresent();
        assertThat(foundItem.get().getProductName()).isEqualTo("Laptop");
        assertThat(foundItem.get().getQuantity()).isEqualTo(2);
    }

    @Test
    void testDeleteInvoiceItemById() {
        invoiceRepository.deleteInvoiceItemById(savedInvoiceItem.getId());

        Optional<InvoiceItem> deletedItem = invoiceRepository.findInvoiceItemById(savedInvoiceItem.getId());
        assertThat(deletedItem).isEmpty();
    }
}
