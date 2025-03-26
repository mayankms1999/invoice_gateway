package com.ims.repository;

import com.ims.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name); // For single product
    List<Product> findAllByName(String name); // For multiple products
}

