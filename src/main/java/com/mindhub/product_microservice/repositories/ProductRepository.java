package com.mindhub.product_microservice.repositories;

import com.mindhub.product_microservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    //Optional<Product> findById(Long id);

    Optional<Product> findByName(String name);
    boolean existsByName(String name);
    void deleteByName(String name);
}
