package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAll(Pageable pageable);

    List<Product> findAll();

    Product getProductById(Long id);

    Product save(Product product);

    boolean existsByName(String name);

    void deleteById(Long id);

    @Query("SELECT p FROM Product p WHERE " +
            "(:name is null or lower(p.name) like lower(concat('%', :name, '%'))) and " +
            "(:price is null or p.price >= :price) and " +
            "(:categoryId is null or p.category.id = :categoryId)")
    Page<Product> searchProducts(@Param("name") String name,
                                 @Param("price") BigDecimal price,
                                 @Param("categoryId") Long categoryId,
                                 Pageable pageable);

}
