package com.ims.repository;

import com.ims.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import jakarta.validation.ConstraintViolationException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest // Runs tests with an in-memory H2 database
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        // Save valid products before running tests
        productRepository.save(Product.builder()
                .name("Laptop")
                .description("High-performance laptop")
                .hsnCode("123456")
                .price(BigDecimal.valueOf(1200.0))
                .build());

        productRepository.save(Product.builder()
                .name("Phone")
                .description("Latest smartphone")
                .hsnCode("654321")
                .price(BigDecimal.valueOf(800.0))
                .build());
    }

    @Test
    void testFindByName() {
        // Fetch a single product by name
        Optional<Product> product = productRepository.findByName("Laptop");

        // Validate result
        assertThat(product).isPresent();
        assertThat(product.get().getName()).isEqualTo("Laptop");
    }

    @Test
    void testFindAllByName() {
        // Insert another Laptop
        productRepository.save(Product.builder()
                .name("Laptop")
                .description("Gaming laptop")
                .hsnCode("123457")
                .price(BigDecimal.valueOf(1500.0))
                .build());

        // Fetch all products named "Laptop"
        List<Product> laptops = productRepository.findAllByName("Laptop");

        // Validate result
        assertThat(laptops).hasSize(2);
        assertThat(laptops).extracting(Product::getName).containsOnly("Laptop");
    }

    @Test
    void testSaveInvalidProduct_ShouldThrowException() {
        // Attempt to save an invalid product (missing required fields)
        Product invalidProduct = Product.builder()
                .name("") // Invalid: name should not be blank
                .description("Short") // Invalid: description too short
                .hsnCode("1234") // Invalid: HSN Code must be 6 digits
                .price(BigDecimal.valueOf(-100.0)) // Invalid: Price must be > 0
                .build();

        // Expect DataIntegrityViolationException due to validation failure
        assertThatThrownBy(() -> productRepository.save(invalidProduct))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
