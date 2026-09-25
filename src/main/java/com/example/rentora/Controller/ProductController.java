package com.example.rentora.Controller;

import com.example.rentora.Api.ApiResponse;
import com.example.rentora.Model.Product;
import com.example.rentora.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping("/get")
    public ResponseEntity getAllProducts() {
        List<Product> products = productService.getAllProducts();
        if(products.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("There is no product"));
        }
        return ResponseEntity.status(200).body(products);
    }


    @PostMapping("/add/{userId}")
    public ResponseEntity addProduct(@PathVariable Integer userId, @Valid @RequestBody Product product, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }

        boolean isAdded = productService.addProduct(userId, product);
        if (!isAdded) {
            return ResponseEntity.status(400).body(new ApiResponse("User (Owner) not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Product added successfully"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity updateProduct(@PathVariable Integer id, @Valid @RequestBody Product product, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }

        boolean isUpdated = productService.updateProduct(id, product);
        if (!isUpdated) {
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));

        }

        return ResponseEntity.status(200).body(new ApiResponse("Product updated successfully"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProduct(@PathVariable Integer id) {
        boolean isDeleted = productService.deleteProduct(id);
        if (!isDeleted) {
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Product deleted successfully"));


    }

    @GetMapping("/search/{name}")
    public ResponseEntity<?> searchProductsByName(@PathVariable String name) {
        List<Product> products = productService.searchProductsByName(name);
        if (products.isEmpty()) {
            return ResponseEntity.status(400).body("No products found with name: " + name);
        }
        return ResponseEntity.status(200).body(products);
    }

    @GetMapping("/calculate-price/{productId}/{days}")
    public ResponseEntity<?> calculateEstimatedPrice(@PathVariable Integer productId, @PathVariable int days) {
        Double totalPrice = productService.calculateEstimatedPrice(productId, days);

        if (totalPrice == null) {
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        }

        if (totalPrice == -1.0) {
            return ResponseEntity.status(400).body(new ApiResponse("Number of days must be greater than zero"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Estimated total price: " + totalPrice + " SR"));
    }


    @PutMapping("/changeavailability/{productId}/{ownerId}")
    public ResponseEntity<?> changeProductAvailability(@PathVariable Integer productId, @PathVariable Integer ownerId) {
        int result = productService.changeProductAvailability(productId, ownerId);

        if (result == 1)
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        if (result == 2)
            return ResponseEntity.status(400).body(new ApiResponse("Unauthorized: You are not the owner of this product"));

        return ResponseEntity.status(200).body(new ApiResponse("Product availability changed successfully"));
    }

    @PutMapping("/changeprice/{productId}/{ownerId}/{newPrice}")
    public ResponseEntity<?> changeProductPrice(@PathVariable Integer productId, @PathVariable Integer ownerId, @PathVariable double newPrice) {
        int result = productService.changeProductPrice(productId, ownerId, newPrice);

        if (result == 1) return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        if (result == 2) return ResponseEntity.status(400).body(new ApiResponse("Unauthorized: You are not the owner of this product"));
        if (result == 3) return ResponseEntity.status(400).body(new ApiResponse("Price must be greater than zero"));

        return ResponseEntity.status(200).body(new ApiResponse("Product price updated successfully"));
    }
}