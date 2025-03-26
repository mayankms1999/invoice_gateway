package com.ims.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.dto.ProductRequest;
import com.ims.dto.ProductResponse;
import com.ims.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductRequest productRequest;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        productRequest = new ProductRequest(null, "Maggie Masala", "Tasty noodles", "654321", BigDecimal.valueOf(12));
        productResponse = new ProductResponse(1L, "Maggie Masala", "Tasty noodles", "654321", BigDecimal.valueOf(12));
    }

    @Test
    void testCreateProduct() throws Exception {
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(productResponse);

        mockMvc.perform(post("/api/product/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Maggie Masala"))
                .andExpect(jsonPath("$.price").value(12));
    }

    @Test
    void testGetProductById() throws Exception {
        when(productService.getProductById(1L)).thenReturn(productResponse);

        mockMvc.perform(get("/api/product/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Maggie Masala"));
    }

    @Test
    void testGetAllProducts() throws Exception {
        List<ProductResponse> productList = Arrays.asList(productResponse);
        when(productService.getAllProducts()).thenReturn(productList);

        mockMvc.perform(get("/api/product/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testGetProductsByName() throws Exception {
        List<ProductResponse> productList = Arrays.asList(productResponse);
        when(productService.getProductsByName("Maggie Masala")).thenReturn(productList);

        mockMvc.perform(get("/api/product/get/by-name")
                        .param("name", "Maggie Masala"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

//    @Test
//    void testDeleteProduct() throws Exception {
//        mockMvc.perform(delete("/api/product/get/1"))
//                .andExpect(status().isNoContent());
//    }
}
//package com.ims.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ims.dto.ProductRequest;
//import com.ims.dto.ProductResponse;
//import com.ims.exception.ResourceNotFoundException;
//import com.ims.service.ProductService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(ProductController.class)
//class ProductControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ProductService productService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private ProductRequest productRequest;
//    private ProductResponse productResponse;
//
//    @BeforeEach
//    void setUp() {
//        productRequest = new ProductRequest(null, "Maggie Masala", "Tasty noodles", "654321", BigDecimal.valueOf(12));
//        productResponse = new ProductResponse(1L, "Maggie Masala", "Tasty noodles", "654321", BigDecimal.valueOf(12));
//    }
//
//    @Test
//    void testCreateProduct_Success() throws Exception {
//        when(productService.createProduct(any(ProductRequest.class))).thenReturn(productResponse);
//
//        mockMvc.perform(post("/api/products/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(productRequest)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("Maggie Masala"))
//                .andExpect(jsonPath("$.price").value(12));
//
//        verify(productService, times(1)).createProduct(any(ProductRequest.class));
//    }
//
//    @Test
//    void testGetProductById_Success() throws Exception {
//        when(productService.getProductById(1L)).thenReturn(productResponse);
//
//        mockMvc.perform(get("/api/products/get/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("Maggie Masala"));
//    }
//
//    @Test
//    void testGetProductById_NotFound() throws Exception {
//        when(productService.getProductById(1L)).thenThrow(new ResourceNotFoundException("Product not found"));
//
//        mockMvc.perform(get("/api/products/get/1"))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Product not found"));
//    }
//
//    @Test
//    void testGetAllProducts_Success() throws Exception {
//        List<ProductResponse> productList = List.of(productResponse);
//        when(productService.getAllProducts()).thenReturn(productList);
//
//        mockMvc.perform(get("/api/products/get-all"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(1));
//    }
//
//    @Test
//    void testGetAllProducts_EmptyList() throws Exception {
//        when(productService.getAllProducts()).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/api/products/get-all"))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void testGetProductsByName_Success() throws Exception {
//        List<ProductResponse> productList = List.of(productResponse);
//        when(productService.getProductsByName("Maggie Masala")).thenReturn(productList);
//
//        mockMvc.perform(get("/api/products/get-by-name")
//                        .param("name", "Maggie Masala"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.size()").value(1));
//    }
//
//    @Test
//    void testDeleteProduct_Success() throws Exception {
//        doNothing().when(productService).deleteProduct(1L);
//
//        mockMvc.perform(delete("/api/products/delete/1"))
//                .andExpect(status().isNoContent());
//
//        verify(productService, times(1)).deleteProduct(1L);
//    }
//
//    @Test
//    void testDeleteProduct_NotFound() throws Exception {
//        doThrow(new ResourceNotFoundException("Product not found")).when(productService).deleteProduct(1L);
//
//        mockMvc.perform(delete("/api/products/delete/1"))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("Product not found"));
//    }
//}
//
