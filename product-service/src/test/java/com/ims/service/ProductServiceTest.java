package com.ims.service;

import com.ims.dto.ProductRequest;
import com.ims.dto.ProductResponse;
import com.ims.exception.ResourceNotFoundException;
import com.ims.model.Product;
import com.ims.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testCreateProduct() {
        // Given
        ProductRequest productRequest = new ProductRequest(
                null, "maggie masala", "High-performance gaming laptop", "654321", BigDecimal.valueOf(12)
        );

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> {
                    Product product = invocation.getArgument(0);
                    return product.toBuilder().id(8L).build();  // Assign ID
                });

        // When
        ProductResponse response = productService.createProduct(productRequest);
        System.out.println("Response: " + response); // Debugging output

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.id()).isEqualTo(8L);
        assertThat(response.name()).isEqualTo("maggie masala");
        assertThat(response.price()).isEqualTo(BigDecimal.valueOf(12));
    }




    @Test
    void testGetAllProducts() {
        // Given
        List<Product> products = List.of(
                new Product(1L, "Laptop", "High-end laptop", "123456", BigDecimal.valueOf(1200.0)),
                new Product(2L, "Phone", "Latest smartphone", "654321", BigDecimal.valueOf(800.0))
        );

        when(productRepository.findAll()).thenReturn(products);

        // When
        List<ProductResponse> responses = productService.getAllProducts();

        // Then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).name()).isEqualTo("Laptop");
        assertThat(responses.get(1).name()).isEqualTo("Phone");
    }

    @Test
    void testGetProductById_ProductExists() {
        // Given
        Product product = new Product(1L, "Laptop", "High-end laptop", "123456", BigDecimal.valueOf(1200.0));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // When
        ProductResponse response = productService.getProductById(1L);

        // Then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Laptop");
    }

    @Test
    void testGetProductById_ProductNotFound() {
        // Given
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> productService.getProductById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found with ID: 99");
    }

    @Test
    void testGetProductsByName_ProductExists() {
        // Given
        List<Product> products = List.of(
                new Product(1L, "Laptop", "Gaming laptop", "123456", BigDecimal.valueOf(1500.0)),
                new Product(2L, "Laptop", "Business laptop", "123457", BigDecimal.valueOf(1800.0))
        );

        when(productRepository.findAllByName("Laptop")).thenReturn(products);

        // When
        List<ProductResponse> responses = productService.getProductsByName("Laptop");

        // Then
        assertThat(responses).hasSize(2);
        assertThat(responses).extracting(ProductResponse::name).containsOnly("Laptop");
    }

    @Test
    void testGetProductsByName_ProductNotFound() {
        // Given
        when(productRepository.findAllByName("Tablet")).thenReturn(List.of());

        // Then
        assertThatThrownBy(() -> productService.getProductsByName("Tablet"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No product found with name: Tablet");
    }

    @Test
    void testDeleteProduct_ProductExists() {
        // Given
        when(productRepository.existsById(1L)).thenReturn(true);

        // When
        productService.deleteProduct(1L);

        // Then
        verify(productRepository).deleteById(1L);
    }

    @Test
    void testDeleteProduct_ProductNotFound() {
        // Given
        when(productRepository.existsById(99L)).thenReturn(false);

        // Then
        assertThatThrownBy(() -> productService.deleteProduct(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found with ID: 99");
    }
}
