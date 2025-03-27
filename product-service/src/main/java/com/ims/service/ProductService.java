package com.ims.service;

import com.ims.dto.ProductRequest;
import com.ims.dto.ProductResponse;
import com.ims.exception.ResourceNotFoundException;
import com.ims.model.Product;
import com.ims.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .hsnCode(productRequest.hsnCode())
                .price(productRequest.price())
                .build();
        Product savedProduct = productRepository.save(product);

        return new ProductResponse(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.getHsnCode(),
                savedProduct.getPrice()
        );
    }

    public Page<ProductResponse> getAllProducts(int page, int size) {
        Page<Product> products = productRepository.findAll(PageRequest.of(page, size));
        return products.map(product -> new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getHsnCode(),
                product.getPrice()
        ));
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getHsnCode(), product.getPrice());
    }

    public Page<ProductResponse> getProductsByName(String name, int page, int size) {
        Page<Product> products = productRepository.findAllByName(name, PageRequest.of(page, size));
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No product found with name: " + name);
        }
        return products.map(product -> new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getHsnCode(),
                product.getPrice()
        ));
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }
}
