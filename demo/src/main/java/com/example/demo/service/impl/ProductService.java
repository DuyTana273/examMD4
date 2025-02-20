package com.example.demo.service.impl;

import com.example.demo.model.Product;
import com.example.demo.model.ProductStatus;
import com.example.demo.repository.IProductRepository;
import com.example.demo.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService implements IProductService {
    @Autowired
    private IProductRepository iProductRepository;


    @Override
    public Page<Product> findAll(Pageable pageable) {
        return iProductRepository.findAll(pageable);
    }

    @Override
    public List<Product> findAll() {
        return iProductRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return iProductRepository.getProductById(id);
    }

    @Override
    public Product save(Product product) {
        if (product.getStatus() == null) {
            product.setStatus(ProductStatus.PENDING);
        }
        return iProductRepository.save(product);
    }

    @Override
    public boolean existsByName(String name) {
        return iProductRepository.existsByName(name);
    }

    @Override
    public void deleteById(Long id) {
        iProductRepository.deleteById(id);
    }

    @Override
    public Page<Product> searchProducts(String name, BigDecimal price, Long categoryId, Pageable pageable) {
        return iProductRepository.searchProducts(name, price, categoryId, pageable);
    }

}
