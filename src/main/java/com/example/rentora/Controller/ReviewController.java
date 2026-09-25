package com.example.rentora.Controller;

import com.example.rentora.Api.ApiResponse;
import com.example.rentora.Model.Review;
import com.example.rentora.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // GET - عرض جميع التقييمات
    @GetMapping("/get")
    public ResponseEntity<?> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        if (reviews.isEmpty()) {
            return ResponseEntity.status(400).body(new ApiResponse("No reviews found"));
        }
        return ResponseEntity.status(200).body(reviews);
    }

    // POST - إضافة تقييم جديد لمستأجر على عملية إيجار محددة
    @PostMapping("/add/{reviewerId}/{rentalId}")
    public ResponseEntity<?> addReview(@PathVariable Integer reviewerId, @PathVariable Integer rentalId, @Valid @RequestBody Review review, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        int result = reviewService.addReview(reviewerId, rentalId, review);

        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("Reviewer user not found"));
        }
        if (result == 2) {
            return ResponseEntity.status(400).body(new ApiResponse("Rented product record not found"));
        }
        if (result == 3) {
            return ResponseEntity.status(400).body(new ApiResponse("User is not authorized to review this rental"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Review added successfully"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Integer id, @Valid @RequestBody Review review, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }

        boolean isUpdated = reviewService.updateReview(id, review);
        if (!isUpdated) {
            return ResponseEntity.status(400).body(new ApiResponse("Review not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Review updated successfully"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer id) {
        boolean isDeleted = reviewService.deleteReview(id);
        if (!isDeleted) {
            return ResponseEntity.status(400).body(new ApiResponse("Review not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Review deleted successfully"));
    }
}