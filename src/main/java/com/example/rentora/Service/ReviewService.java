package com.example.rentora.Service;

import com.example.rentora.Model.RentedProduct;
import com.example.rentora.Model.Review;
import com.example.rentora.Model.User;
import com.example.rentora.Repository.RentedProductRepository;
import com.example.rentora.Repository.ReviewRepository;
import com.example.rentora.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final RentedProductRepository rentedProductRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public int addReview(Integer reviewerId, Integer rentalId, Review review) {
        User reviewer = userRepository.findUserById(reviewerId);
        if (reviewer == null) {
            return 1;
        }

        RentedProduct rentedProduct = rentedProductRepository.findRentedProductById(rentalId);
        if (rentedProduct == null) {
            return 2;
        }

        if (!rentedProduct.getRenterId().equals(reviewerId)) {
            return 3;
        }

        review.setReviewerId(reviewerId);
        review.setRentalId(rentalId);

        reviewRepository.save(review);
        return 0;
    }

    public boolean updateReview(Integer id, Review review) {
        Review oldReview = reviewRepository.findReviewById(id);
        if (oldReview == null) {
            return false;
        }

        oldReview.setRating(review.getRating());
        oldReview.setComment(review.getComment());

        reviewRepository.save(oldReview);
        return true;
    }

    public boolean deleteReview(Integer id) {
        Review review = reviewRepository.findReviewById(id);
        if (review == null) {
            return false;
        }

        reviewRepository.delete(review);
        return true;
    }


}