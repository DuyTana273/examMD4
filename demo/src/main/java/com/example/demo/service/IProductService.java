package com.example.demo.service;

import com.example.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface IProductService {
    Page<Product> findAll(Pageable pageable);

    List<Product> findAll();

    Product getProductById(Long id);

    Product save(Product product);

    boolean existsByName(String name);

    void deleteById(Long id);

    Page<Product> searchProducts(String name, BigDecimal price, Long categoryId, Pageable pageable);
}
