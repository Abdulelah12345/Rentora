package com.example.rentora.Repository;

import com.example.rentora.Model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface RentalRepository extends JpaRepository<Rental,Integer> {
    Rental findRentalById(Integer id);

    Rental findRentalByRequestId(Integer requestId);

}
