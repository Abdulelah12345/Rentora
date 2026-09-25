package com.example.rentora.Service;

import com.example.rentora.Model.Product;
import com.example.rentora.Model.RentedProduct;
import com.example.rentora.Model.User;
import com.example.rentora.Repository.ProductRepository;
import com.example.rentora.Repository.RentedProductRepository;
import com.example.rentora.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RentedProductService {

    private final RentedProductRepository rentedProductRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    public List<RentedProduct> getAllRentedProducts() {
        return rentedProductRepository.findAll();
    }


    public int rentProduct(Integer renterId, Integer productId, RentedProduct rentedProduct) {

        User renter = userRepository.findUserById(renterId);
        if (renter == null) {
            return 1;
        }


        Product product = productRepository.findProductById(productId);
        if (product == null) {
            return 2;
        }


        if (product.getAvailable() != null && !product.getAvailable()) {
            return 3;
        }


        rentedProduct.setRenterId(renterId);
        rentedProduct.setProductId(productId);


        product.setAvailable(false);
        productRepository.save(product);

        rentedProductRepository.save(rentedProduct);
        return 0;
    }

    public boolean updateRentedProduct(Integer id, RentedProduct rentedProduct) {
        RentedProduct oldRentedProduct = rentedProductRepository.findRentedProductById(id);
        if (oldRentedProduct == null) {
            return false;
        }

        oldRentedProduct.setStartDate(rentedProduct.getStartDate());
        oldRentedProduct.setEndDate(rentedProduct.getEndDate());

        rentedProductRepository.save(oldRentedProduct);
        return true;
    }


    public boolean returnRentedProduct(Integer id) {
        RentedProduct rentedProduct = rentedProductRepository.findRentedProductById(id);
        if (rentedProduct == null) {
            return false;
        }


        Product product = productRepository.findProductById(rentedProduct.getProductId());
        if (product != null) {
            product.setAvailable(true);
            productRepository.save(product);
        }

        rentedProductRepository.delete(rentedProduct);
        return true;
    }
}