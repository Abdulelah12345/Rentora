package com.example.rentora.Controller;

import com.example.rentora.Api.ApiResponse;
import com.example.rentora.Model.RentalRequest;
import com.example.rentora.Service.ProductService;
import com.example.rentora.Service.RentalRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rental-request")
@RequiredArgsConstructor
public class RentalRequestController {

    private final RentalRequestService rentalRequestService;


    @GetMapping("/get")
    public ResponseEntity<?> getAllRequests() {
        List<RentalRequest> requests = rentalRequestService.getAllRequests();
        if (requests.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("No rental requests found"));
        }
        return ResponseEntity.status(200).body(requests);
    }


    @PostMapping("/add/{renterId}/{productId}")
    public ResponseEntity<?> submitRequest(@PathVariable Integer renterId, @PathVariable Integer productId, @Valid @RequestBody RentalRequest request, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        int result = rentalRequestService.addRentalRequest(renterId, productId, request);
        if (result == 1)
            return ResponseEntity.status(400).body(new ApiResponse("Renter user not found"));
        if (result == 2)
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        if (result == 3)
            return ResponseEntity.status(400).body(new ApiResponse("Product is currently not available"));
        if (result == 4)
            return ResponseEntity.status(400).body(new ApiResponse("End date cannot be before start date"));

        return ResponseEntity.status(200).body(new ApiResponse("Rental request submitted successfully"));
    }

    @PutMapping("/accept/{requestId}/{ownerID}")
    public ResponseEntity<?> acceptRequest(@PathVariable Integer requestId,@PathVariable Integer ownerID) {
        int result = rentalRequestService.acceptRequest(requestId,ownerID);
        if (result == 1)
            return ResponseEntity.status(400).body(new ApiResponse("Rental request not found"));
        if (result == 2)
            return ResponseEntity.status(400).body(new ApiResponse("Request already processed"));
        if (result == 3)
            return ResponseEntity.status(400).body(new ApiResponse("Product is no longer available"));
      if (result==5) {
          return ResponseEntity.status(400).body(new ApiResponse("Owner cannot rent their product "));
      }
      return ResponseEntity.status(200).body(new ApiResponse("Rental request accepted and active rental created successfully"));
    }

    @PutMapping("/reject/{requestId}")
    public ResponseEntity<?> rejectRequest(@PathVariable Integer requestId) {
        boolean isRejected = rentalRequestService.rejectRequest(requestId);
        if (!isRejected) {
            return ResponseEntity.status(400).body(new ApiResponse("Rental request not found or already processed"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Rental request rejected successfully"));
    }

    @PutMapping("/cancel/{requestId}/{renterId}")
    public ResponseEntity<?> cancelRequest(@PathVariable Integer requestId, @PathVariable Integer renterId) {
        int result = rentalRequestService.cancelRequest(requestId, renterId);

        if (result == 1)
            return ResponseEntity.status(400).body(new ApiResponse("Rental request not found"));
        if (result == 2)
            return ResponseEntity.status(400).body(new ApiResponse("You can only cancel your own requests"));
        if (result == 3)
            return ResponseEntity.status(400).body(new ApiResponse("Cannot cancel a request that is already processed"));

        return ResponseEntity.status(200).body(new ApiResponse("Rental request cancelled successfully"));
    }


    @GetMapping("/renterpending/{renterId}")
    public ResponseEntity<?> getRenterPendingRequests(@PathVariable Integer renterId) {
        List<RentalRequest> requests = rentalRequestService.getRenterPendingRequests(renterId);
        if(requests.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("there is no pending request"));
        }

        return ResponseEntity.status(200).body(requests);
    }


    @PutMapping("/rejectallpending/{productId}/{ownerId}")
    public ResponseEntity<?> rejectAllPendingRequests(@PathVariable Integer productId, @PathVariable Integer ownerId) {
        int result = rentalRequestService.rejectAllPendingRequests(productId, ownerId);

        if (result == 1) return ResponseEntity.status(404).body(new ApiResponse("Product not found"));
        if (result == 2) return ResponseEntity.status(403).body(new ApiResponse("Unauthorized: Not the product owner"));
        if (result == 3) return ResponseEntity.status(400).body(new ApiResponse("No pending requests to reject for this product"));

        return ResponseEntity.status(200).body(new ApiResponse("All pending requests rejected successfully"));
    }


    @PutMapping("/updatedates/{requestId}/{renterId}")
    public ResponseEntity<?> updateRequestDates(@PathVariable Integer requestId, @PathVariable Integer renterId, @Valid @RequestBody RentalRequest request, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        int result = rentalRequestService.updateRequestDates(requestId, renterId, request);

        if (result == 1)
            return ResponseEntity.status(400).body(new ApiResponse("Rental request not found"));
        if (result == 2)
            return ResponseEntity.status(400).body(new ApiResponse("You can only update your own requests"));
        if (result == 3)
            return ResponseEntity.status(400).body(new ApiResponse("Cannot update dates for a request that is already processed"));
        if (result == 4)
            return ResponseEntity.status(400).body(new ApiResponse("End date cannot be before start date"));

        return ResponseEntity.status(200).body(new ApiResponse("Rental request dates updated and notification sent to owner successfully"));
    }


    @PostMapping("/ask/{productId}/{renterId}")
    public ResponseEntity<?> askProductQuestion(@PathVariable Integer productId, @PathVariable Integer renterId, @RequestBody String question) {

        if (question == null || question.isBlank()) {
            return ResponseEntity.status(400).body(new ApiResponse("Question cannot be empty"));
        }

        int result = rentalRequestService.askProductQuestion(productId, renterId, question);

        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("Product not found"));
        }
        if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Question sent to the product owner successfully"));
    }


}
