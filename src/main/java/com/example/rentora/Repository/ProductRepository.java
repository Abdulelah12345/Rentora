package com.example.rentora.Repository;

import com.example.rentora.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ProductRepository extends JpaRepository<Product,Integer> {
    Product findProductById(Integer id);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByOwnerId(Integer ownerId);

}
