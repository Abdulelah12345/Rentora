package com.example.rentora.Repository;

import com.example.rentora.Model.RentedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface RentedProductRepository extends JpaRepository<RentedProduct,Integer> {

    RentedProduct findRentedProductById(Integer id);
    boolean existsByProductId(Integer productId);
    RentedProduct findByProductId(Integer productId);
}
