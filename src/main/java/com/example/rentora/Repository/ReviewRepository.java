package com.example.rentora.Repository;

import com.example.rentora.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ReviewRepository extends JpaRepository<Review,Integer> {
    Review findReviewById(Integer id);

    // البحث عن التقييمات الخاصة بسجل تأجير معين
    List<Review> findReviewsByRentalId(Integer rentalId);

    // البحث عن التقييمات التي كتبها مستخدم معين
    List<Review> findReviewsByReviewerId(Integer reviewerId);
}
