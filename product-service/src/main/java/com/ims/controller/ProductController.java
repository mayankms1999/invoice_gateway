package com.ims.controller;

import com.ims.dto.ProductRequest;
import com.ims.dto.ProductResponse;
import com.ims.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    // POST: Create a new product
    @PostMapping("/create")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        log.info("Received request to create product: {}", productRequest);
        ProductResponse createdProduct = productService.createProduct(productRequest);
        log.info("Product created successfully: {}", createdProduct);
        return ResponseEntity.status(201).body(createdProduct);
    }

    // GET: Retrieve all products
    @GetMapping("/get-all")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.info("Fetching all products...");
        List<ProductResponse> products = productService.getAllProducts();
        if (products.isEmpty()) {
            log.warn("No products found in the database.");
            return ResponseEntity.noContent().build();
        }
        log.info("Returning {} products", products.size());
        return ResponseEntity.ok(products);
    }

    // GET: Retrieve a product by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") Long id) {
        log.info("Fetching product with ID: {}", id);
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    // GET: Retrieve products by name
    @GetMapping("/get/by-name")
    public ResponseEntity<List<ProductResponse>> getProductsByName(@RequestParam(name = "name") String name) {
        if (name == null || name.isBlank()) {
            log.warn("Product name is empty or null");
            return ResponseEntity.badRequest().build();
        }
        log.info("Fetching products with name: {}", name);
        List<ProductResponse> products = productService.getProductsByName(name);
        return ResponseEntity.ok(products);
    }

    // DELETE: Remove a product by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        log.info("Deleting product with ID: {}", id);
        productService.deleteProduct(id);
        log.info("Product with ID {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}
