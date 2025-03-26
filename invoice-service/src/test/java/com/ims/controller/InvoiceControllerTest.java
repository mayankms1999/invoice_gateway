package com.ims.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.dto.InvoiceRequest;
import com.ims.dto.InvoiceResponse;
import com.ims.dto.ProductRequest;
import com.ims.dto.UserDetailRequest;
import com.ims.model.UserDetail;
import com.ims.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(InvoiceController.class)
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // For JSON serialization

    @MockBean
    private InvoiceService invoiceService;

    private InvoiceResponse mockInvoiceResponse;
    private InvoiceRequest mockInvoiceRequest;

    @BeforeEach
    void setUp() {
        // Creating UserDetailRequest
        UserDetailRequest userDetailRequest = new UserDetailRequest("John Doe", "john.doe@example.com", "1234567890");

        // Creating ProductRequest
        ProductRequest product = new ProductRequest(1L, "Laptop", 2, 1000.0);
        List<ProductRequest> products = List.of(product);

        // Fix: Convert UserDetailRequest to UserDetail
        UserDetail userDetail = new UserDetail(1L,"John Doe", "john.doe@example.com", "1234567890");

        mockInvoiceRequest = new InvoiceRequest(userDetailRequest, products);

        mockInvoiceResponse = new InvoiceResponse(
                1L, // Corrected ID type (Long)
                "INV-1001", // Corrected invoiceNumber type (String)
                LocalDateTime.now(), // Corrected createdAt type
                Collections.emptyList(), // Corrected items type
                userDetail // UserDetail instead of UserDetailRequest
        );
    }


//    @Test
//    void testCreateInvoice() throws Exception {
//        when(invoiceService.createInvoice(any(InvoiceRequest.class))).thenReturn(mockInvoiceResponse);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/invoices/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(mockInvoiceRequest))) // Corrected here
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.invoiceNumber").value("INV-1001"))
//                .andExpect(jsonPath("$.user.email").value("john.doe@example.com"));
//    }

    @Test
    void testGetInvoiceById() throws Exception {
        when(invoiceService.getInvoiceById(1L)).thenReturn(mockInvoiceResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/invoices/get/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceNumber").value("INV-1001"))
                .andExpect(jsonPath("$.user.email").value("john.doe@example.com"));
    }

    @Test
    void testGetAllInvoices() throws Exception {
        when(invoiceService.getAllInvoices()).thenReturn(List.of(mockInvoiceResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/invoices/get-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].invoiceNumber").value("INV-1001"))
                .andExpect(jsonPath("$[0].user.email").value("john.doe@example.com"));
    }

    @Test
    void testDeleteInvoiceItem() throws Exception {
        doNothing().when(invoiceService).deleteInvoiceItem(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/invoices/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // 204 No Content
    }
}
