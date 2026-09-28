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
    private final EmailService emailService;

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
                rentedProduct.setStatus("COMPLETED");
                rentedProductRepository.save(rentedProduct);
            }
        }

        return 0;
    }

    public int extendRentalByRenter(Integer rentalId, Integer renterId, int extraDays) {
        Rental rental = rentalRepository.findRentalById(rentalId);
        if (rental == null) {
            return 1;
        }

        if (!rental.getStatus().equals("ACTIVE")) {
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


        rental.setRequestedExtraDays(extraDays);
        rental.setExtensionStatus("PENDING_EXTENSION");
        rentalRepository.save(rental);
        return 0;
    }

    public int approveRentalExtension(Integer rentalId, Integer ownerId) {
        Rental rental = rentalRepository.findRentalById(rentalId);
        if (rental == null) {
            return 1;
        }

        if (!"PENDING_EXTENSION".equalsIgnoreCase(rental.getExtensionStatus())) {
            return 2;
        }

        RentalRequest request = rentalRequestRepository.findRentalRequestById(rental.getRequestId());
        if (request == null) {
            return 1;
        }

        Product product = productRepository.findProductById(request.getProductId());
        if (product == null || !product.getOwnerId().equals(ownerId)) {
            return 3;
        }

        int extraDays = rental.getRequestedExtraDays();
        rental.setEndDate(rental.getEndDate().plusDays(extraDays));

        rental.setRequestedExtraDays(0);
        rental.setExtensionStatus("APPROVED");
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


    public int reportIssue(Integer rentalId, Integer renterId, String description) {
        Rental rental = rentalRepository.findRentalById(rentalId);
        if (rental == null) {
            return 1;
        }


        RentalRequest request = rentalRequestRepository.findRentalRequestById(rental.getRequestId());
        if (request == null) {
            return 1;
        }


        if (!request.getRenterId().equals(renterId)) {
            return 2;
        }


        rental.setStatus("REPORTED");
        rentalRepository.save(rental);


        String subject = "Rentora - بلاغ على حجز رقم #" + rentalId;
        String body = "تم استلام بلاغ جديد للحجز رقم (" + rentalId + ") من المستأجر برقم معرف (" + renterId + "):\n\nالوصف: " + description;

        emailService.sendEmail("alwadaniabdulelah@gmail.com", subject, body);

        return 0;
    }




}
