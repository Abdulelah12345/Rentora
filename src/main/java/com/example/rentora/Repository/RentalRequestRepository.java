package com.example.rentora.Repository;

import com.example.rentora.Model.RentalRequest;
import com.example.rentora.Model.RentedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface RentalRequestRepository extends JpaRepository<RentalRequest,Integer> {
    RentalRequest findRentalRequestById(Integer id);
    List<RentalRequest> findByProductIdAndStatus(Integer productId, String status);

    List<RentalRequest> findRentalRequestsByRenterId(Integer renterId);

    List<RentalRequest> findRentalRequestsByProductId(Integer productId);

    List<RentalRequest> findRentalRequestsByStatus(String status);
}
