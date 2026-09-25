package com.example.rentora.Service;

import com.example.rentora.Model.Product;
import com.example.rentora.Model.User;
import com.example.rentora.Repository.ProductRepository;
import com.example.rentora.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public boolean addProduct(Integer userId, Product product) {

        User user = userRepository.findUserById(userId);
        if (user == null) {
            return false;
        }


        product.setOwnerId(userId);
        productRepository.save(product);
        return true;
    }

    public boolean updateProduct(Integer id, Product product) {
        Product oldProduct = productRepository.findProductById(id);
        if (oldProduct == null) {
            return false;
        }

        oldProduct.setName(product.getName());
        oldProduct.setDescription(product.getDescription());
        oldProduct.setCategory(product.getCategory());
        oldProduct.setPricePerDay(product.getPricePerDay());
        oldProduct.setDeposit(product.getDeposit());
        oldProduct.setAvailable(product.getAvailable());

        productRepository.save(oldProduct);
        return true;
    }

    public boolean deleteProduct(Integer id) {
        Product oldProduct = productRepository.findProductById(id);
        if (oldProduct == null) {
            return false;
        }

        productRepository.delete(oldProduct);
        return true;
    }
    public List<Product> searchProductsByName(String name) {
        if (name == null) {
            return null;
        }
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public Double calculateEstimatedPrice(Integer productId, int days) {

        Product product = productRepository.findProductById(productId);
        if (product == null) {
            return null;
        }

        if (days <= 0) {
            return -1.0; //check it
        }


        double total = (days * product.getPricePerDay()) + product.getDeposit();

        return total;
    }


    public int changeProductAvailability(Integer productId, Integer ownerId) {
        Product product = productRepository.findProductById(productId);
        if (product == null) {
            return 1;
        }

        if (!product.getOwnerId().equals(ownerId)) {
            return 2;
        }


        if (product.getAvailable().equals(true)) {
            product.setAvailable(false);
        } else {
            product.setAvailable(true);
        }

        productRepository.save(product);
        return 0;
    }


    public int changeProductPrice(Integer productId, Integer ownerId, double newPrice) {
        Product product = productRepository.findProductById(productId);
        if (product == null) {
            return 1;
        }


        if (!product.getOwnerId().equals(ownerId)) {
            return 2;
        }


        if (newPrice <= 0) {
            return 3;
        }

        product.setPricePerDay(newPrice);
        productRepository.save(product);
        return 0;
    }
}