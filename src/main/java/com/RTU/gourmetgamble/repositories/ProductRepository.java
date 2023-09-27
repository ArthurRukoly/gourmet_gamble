package com.RTU.gourmetgamble.repositories;

import com.RTU.gourmetgamble.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
//    @Query("SELECT p.id FROM product p WHERE p.name = :name")
//    Long findProductIdByName(@Param("name") String productName);

    @Query("SELECT p FROM Product p WHERE p.name = :name")
    Product findProductByName(@Param("name") String productName);

    Product findByName(String name);

    @Query("SELECT p FROM Product p")
    List<Product> getAllProducts();

    @Query("SELECT p.id FROM Product p WHERE p.name = :name")
    Long getIdFromName(@Param("name") String name);

    Optional<Product> findById(Long id);
}