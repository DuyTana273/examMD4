package com.example.demo.model.dto;

import com.example.demo.model.Category;
import com.example.demo.model.ProductStatus;
import jakarta.persistence.*;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

public class ProductDTO implements Validator {
    private Long id;
    private String name;
    private BigDecimal price;
    private ProductStatus status;
    private Category category;

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, BigDecimal price, ProductStatus status, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.status = status;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProductDTO productDTO = (ProductDTO) target;

        // Validate name
        String name = productDTO.getName();
        if (name.trim().isEmpty()) {
            errors.rejectValue("name", "input.null");
        } else if (name.length() < 5 || name.length() > 50) {
            errors.rejectValue("name", "", "Tên sản phẩm từ 5 đến 50 ký tự!");
        }

        // Validate price
        BigDecimal price = productDTO.getPrice();
        if (price == null) {
            errors.rejectValue("price", "input.null", "Giá không được để trống");
        } else if (price.compareTo(new BigDecimal("100000")) < 0 || price.compareTo(new BigDecimal("100000000")) > 0) {
            errors.rejectValue("price", "", "Giá trị giá phải nằm trong khoảng từ 100,000 VND đến 100,000,000 VND");
        }

        // Validate category
        Category category = productDTO.getCategory();
        if(category == null) {
            errors.rejectValue("category", "input.null");
        }

    }
}
