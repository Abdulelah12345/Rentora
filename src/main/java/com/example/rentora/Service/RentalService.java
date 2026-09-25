package com.example.rentora.Service;

import com.example.rentora.Model.Product;
import com.example.rentora.Model.Rental;
import com.example.rentora.Model.RentalRequest;
import com.example.rentora.Model.RentedProduct;
import com.example.rentora.Repository.ProductRepository;
import com.example.rentora.Repository.RentalRepository;
import com.example.rentora.Repository.RentalRequestRepository;
import com.example.rentora.Repository.RentedProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final RentalRequestRepository rentalRequestRepository;
    private final ProductRepository productRepository;
    private final RentedProductRepository rentedProductRepository;

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }


    public int completeRental(Integer rentalId) {
        Rental rental = rentalRepository.findRentalById(rentalId);
        if (rental == null) {
            return 1;
        }

        if (!"ACTIVE".equalsIgnoreCase(rental.getStatus())) {
            return 2;
        }


        rental.setStatus("COMPLETED");
        rentalRepository.save(rental);


        RentalRequest request = rentalRequestRepository.findRentalRequestById(rental.getRequestId());
        if (request != null) {


            Product product = productRepository.findProductById(request.getProductId());
            if (product != null) {
                product.setAvailable(true);
                productRepository.save(product);
            }


            RentedProduct rentedProduct = rentedProductRepository.findByProductId(request.getProductId());
            if (rentedProduct != null) {
                rentedProductRepository.delete(rentedProduct);
            }
        }

        return 0;
    }

    public int extendRentalByRenter(Integer rentalId, Integer renterId, int extraDays) {
        Rental rental = rentalRepository.findRentalById(rentalId);
        if (rental == null) {
            return 1;
        }

        if (!rental.getStatus().equalsIgnoreCase("ACTIVE")) {
            return 2;
        }


        RentalRequest request = rentalRequestRepository.findRentalRequestById(rental.getRequestId());
        if (request == null) {
            return 1;
        }


        if (!request.getRenterId().equals(renterId)) {
            return 3;
        }

        if (extraDays <= 0) {
            return 4;
        }


        rental.setEndDate(rental.getEndDate().plusDays(extraDays));
        rentalRepository.save(rental);
        return 0;
    }


    public double getOwnerEarnings(Integer ownerId) {
        double total = 0.0;
        List<Rental> rentals = rentalRepository.findAll();

        for (int i = 0; i < rentals.size(); i++) {
            Rental rental = rentals.get(i);
            RentalRequest request = rentalRequestRepository.findRentalRequestById(rental.getRequestId());

            if (request != null) {
                Product product = productRepository.findProductById(request.getProductId());
                if (product != null && product.getOwnerId().equals(ownerId)) {
                    total += rental.getTotalPrice();
                }
            }
        }
        return total;
    }
}