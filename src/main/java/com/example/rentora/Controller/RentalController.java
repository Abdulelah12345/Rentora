package com.example.rentora.Controller;

import com.example.rentora.Api.ApiResponse;
import com.example.rentora.Model.Rental;
import com.example.rentora.Service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rental")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        if (rentals.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("No rentals found"));
        }
        return ResponseEntity.status(200).body(rentals);
    }

    @PutMapping("/complete/{rentalId}")
    public ResponseEntity<?> completeRental(@PathVariable Integer rentalId) {
        int result = rentalService.completeRental(rentalId);

        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("Rental contract not found"));
        }
        if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("Rental contract is not active"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Rental completed successfully and product is now available again"));
    }

    @PutMapping("/extenddays/{rentalId}/{renterId}/{extraDays}")
    public ResponseEntity<?> extendRentalByRenter(@PathVariable Integer rentalId, @PathVariable Integer renterId, @PathVariable int extraDays) {
        int result = rentalService.extendRentalByRenter(rentalId, renterId, extraDays);

        if (result == 1) return ResponseEntity.status(400).body(new ApiResponse("Rental or request not found"));
        if (result == 2) return ResponseEntity.status(400).body(new ApiResponse("Can only extend ACTIVE rentals"));
        if (result == 3) return ResponseEntity.status(400).body(new ApiResponse("Unauthorized: Only the actual renter can extend this rental"));
        if (result == 4) return ResponseEntity.status(400).body(new ApiResponse("Extra days must be greater than zero"));

        return ResponseEntity.status(200).body(new ApiResponse("Rental extended successfully"));
    }

    @GetMapping("/earnings/{ownerId}")
    public ResponseEntity<?> getOwnerEarnings(@PathVariable Integer ownerId) {
        double earnings = rentalService.getOwnerEarnings(ownerId);
        return ResponseEntity.status(200).body(new ApiResponse("إجمالي أرباح المالك هي: " + earnings));
    }
}