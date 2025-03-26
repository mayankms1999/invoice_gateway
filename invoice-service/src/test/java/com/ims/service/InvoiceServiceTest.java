package com.ims.service;

import com.ims.dto.InvoiceRequest;
import com.ims.dto.InvoiceResponse;
import com.ims.exception.InvoiceNotFoundException;
import com.ims.model.Invoice;
import com.ims.model.InvoiceItem;
import com.ims.model.UserDetail;
import com.ims.repository.InvoiceRepository;
import com.ims.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    private Invoice invoice;
    private InvoiceItem invoiceItem;

    @BeforeEach
    void setUp() {
        UserDetail userDetail = UserDetail.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();

        invoiceItem = InvoiceItem.builder()
                .id(1L)
                .productId(101L)
                .productName("Test Product")
                .quantity(2)
                .price(100.0)
                .build();

        invoice = Invoice.builder()
                .id(1L)
                .invoiceNumber(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .items(Collections.singletonList(invoiceItem))
                .userDetail(userDetail)
                .build();
    }

    @Test
    void shouldReturnInvoiceById() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        InvoiceResponse response = invoiceService.getInvoiceById(1L);

        assertNotNull(response);
        assertEquals(invoice.getId(), response.getId());
        verify(invoiceRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenInvoiceNotFound() {
        when(invoiceRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(InvoiceNotFoundException.class, () -> invoiceService.getInvoiceById(99L));
        assertEquals("Invoice not found with ID: 99", exception.getMessage());
    }

    @Test
    void shouldReturnAllInvoices() {
        when(invoiceRepository.findAll()).thenReturn(List.of(invoice));
        List<InvoiceResponse> responses = invoiceService.getAllInvoices();

        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        verify(invoiceRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteInvoiceItemById() {
        when(invoiceRepository.findInvoiceItemById(1L)).thenReturn(Optional.of(invoiceItem));
        doNothing().when(invoiceRepository).deleteInvoiceItemById(1L);
        System.out.println("Mocked invoice item ID: " + invoiceItem.getId());
        assertDoesNotThrow(() -> invoiceService.deleteInvoiceItem(1L));
        verify(invoiceRepository, times(1)).deleteInvoiceItemById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentInvoiceItem() {
        when(invoiceRepository.findInvoiceItemById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(InvoiceNotFoundException.class, () -> invoiceService.deleteInvoiceItem(99L));
        assertEquals("Invoice item not found with ID: 99", exception.getMessage());
    }
}
