package com.example.rentora.Controller;

import com.example.rentora.Api.ApiResponse;
import com.example.rentora.Model.RentedProduct;
import com.example.rentora.Service.RentedProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rented-product")
@RequiredArgsConstructor
public class RentedProductController {

    private final RentedProductService rentedProductService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllRentedProducts() {
        List<RentedProduct> list = rentedProductService.getAllRentedProducts();
        if (list.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("there is no rented product"));
        }
        return ResponseEntity.status(200).body(list);
    }

    // تم تحديث الرابط ليمرر renterId و productId
    @PostMapping("/add/{renterId}/{productId}")
    public ResponseEntity<?> rentProduct(@PathVariable Integer renterId, @PathVariable Integer productId, @Valid @RequestBody RentedProduct rentedProduct, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }

        int result = rentedProductService.rentProduct(renterId, productId, rentedProduct);
        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("Renter user not found"));
        }
        if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        }
        if (result == 3) {
            return ResponseEntity.status(400).body(new ApiResponse("Product is currently already rented"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Product rented successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRentedProduct(@PathVariable Integer id, @Valid @RequestBody RentedProduct rentedProduct, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        boolean isUpdated = rentedProductService.updateRentedProduct(id, rentedProduct);
        if (!isUpdated) {
            return ResponseEntity.status(400).body(new ApiResponse("Rented product record not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Rented product record updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> returnRentedProduct(@PathVariable Integer id) {
        boolean isReturned = rentedProductService.returnRentedProduct(id);
        if (!isReturned) {
            return ResponseEntity.status(400).body(new ApiResponse("Rented product record not found"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Product returned and rented record deleted successfully"));
    }
}