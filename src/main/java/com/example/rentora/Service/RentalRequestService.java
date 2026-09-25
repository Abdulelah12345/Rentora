package com.example.rentora.Service;

import com.example.rentora.Model.Product;
import com.example.rentora.Model.Rental;
import com.example.rentora.Model.RentalRequest;
import com.example.rentora.Model.RentedProduct;
import com.example.rentora.Model.User;
import com.example.rentora.Repository.ProductRepository;
import com.example.rentora.Repository.RentalRepository;
import com.example.rentora.Repository.RentalRequestRepository;
import com.example.rentora.Repository.RentedProductRepository;
import com.example.rentora.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalRequestService {

    private final RentalRequestRepository rentalRequestRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final RentedProductRepository rentedProductRepository;
    private final EmailService emailService;

    public List<RentalRequest> getAllRequests() {
        return rentalRequestRepository.findAll();
    }

    public int addRentalRequest(Integer renterId, Integer productId, RentalRequest request) {
        User renter = userRepository.findUserById(renterId);
        if (renter == null) {
            return 1;
        }

        Product product = productRepository.findProductById(productId);
        if (product == null) {
            return 2;
        }

        if (rentedProductRepository.existsByProductId(productId)) {
            return 3;
        }

        if (product.getAvailable() != null && !product.getAvailable()) {
            return 3;
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            return 4;
        }
        if (product.getOwnerId().equals(renterId)) {
            return 5;
        }

        request.setRenterId(renterId);
        request.setProductId(productId);
        request.setStatus("PENDING");

        rentalRequestRepository.save(request);

        String subject = "Rentora - تم تقديم طلب الاستئجار بنجاح";
        String body = "أهلاً " + renter.getName() + "،\n\n" +
                "تم استلام طلبك لاستئجار المنتج (" + product.getName() + ") بنجاح.\n" +
                "تاريخ البداية: " + request.getStartDate() + "\n" +
                "تاريخ النهاية: " + request.getEndDate() + "\n\n" +
                "طلبك حالياً قيد المراجعة من قبل المالك.";

        emailService.sendEmail(renter.getEmail(), subject, body);
        return 0;
    }

    public int acceptRequest(Integer requestId, Integer ownerId) {
        RentalRequest request = rentalRequestRepository.findRentalRequestById(requestId);
        if (request == null) {
            return 1;
        }

        if (!"PENDING".equalsIgnoreCase(request.getStatus())) {
            return 2;
        }

        Product product = productRepository.findProductById(request.getProductId());
        if (product == null || (product.getAvailable() != null && !product.getAvailable())) {
            return 3;
        }


        if (!product.getOwnerId().equals(ownerId)) {
            return 4;
        }


        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        if (days <= 0) days = 1;

        double totalPrice = (days * product.getPricePerDay()) + product.getDeposit();


        request.setStatus("APPROVED");
        rentalRequestRepository.save(request);


        List<RentalRequest> pendingRequests = rentalRequestRepository.findByProductIdAndStatus(request.getProductId(), "PENDING");
        for (RentalRequest pendingReq : pendingRequests) {
            pendingReq.setStatus("REJECTED");
            rentalRequestRepository.save(pendingReq);

        }


        Rental rental = new Rental();
        rental.setRequestId(requestId);
        rental.setStartDate(request.getStartDate());
        rental.setEndDate(request.getEndDate());
        rental.setStatus("ACTIVE");
        rental.setTotalPrice(totalPrice);
        rental.setLateFee(0.0);
        rentalRepository.save(rental);


        RentedProduct rentedProduct = new RentedProduct();
        rentedProduct.setProductId(request.getProductId());
        rentedProduct.setRenterId(request.getRenterId());
        rentedProduct.setStartDate(request.getStartDate());
        rentedProduct.setEndDate(request.getEndDate());
        rentedProductRepository.save(rentedProduct);


        product.setAvailable(false);
        productRepository.save(product);

        return 0;
    }

    public boolean rejectRequest(Integer requestId) {
        RentalRequest request = rentalRequestRepository.findRentalRequestById(requestId);
        if (request == null) {
            return false;
        }

        request.setStatus("REJECTED");
        rentalRequestRepository.save(request);
        return true;
    }

    public int cancelRequest(Integer requestId, Integer renterId) {
        RentalRequest request = rentalRequestRepository.findRentalRequestById(requestId);
        if (request == null) {
            return 1;
        }
        if (!request.getRenterId().equals(renterId)) {
            return 2;
        }
        if (!request.getStatus().equals("PENDING")) {
            return 3;
        }

        request.setStatus("CANCELLED");
        rentalRequestRepository.save(request);
        return 0;
    }


    public List<RentalRequest> getRenterPendingRequests(Integer renterId) {
        List<RentalRequest> allRequests = rentalRequestRepository.findAll();
        List<RentalRequest> pendingRequests = new ArrayList<>();

        for (int i = 0; i < allRequests.size(); i++) {
            RentalRequest req = allRequests.get(i);
            if (req.getRenterId().equals(renterId) && req.getStatus().equals("PENDING")) {
                pendingRequests.add(req);
            }
        }
        return pendingRequests;
    }


    public int rejectAllPendingRequests(Integer productId, Integer ownerId) {
        Product product = productRepository.findProductById(productId);
        if (product == null) {
            return 1;
        }

        if (!product.getOwnerId().equals(ownerId)) {
            return 2;
        }

        List<RentalRequest> allRequests = rentalRequestRepository.findAll();
        boolean foundPending = false;

        for (int i = 0; i < allRequests.size(); i++) {
            RentalRequest req = allRequests.get(i);
            if (req.getProductId().equals(productId) && req.getStatus().equalsIgnoreCase("PENDING")) {
                req.setStatus("REJECTED");
                rentalRequestRepository.save(req);
                foundPending = true;
            }
        }

        if (!foundPending) {
            return 3;
        }

        return 0;
    }


}