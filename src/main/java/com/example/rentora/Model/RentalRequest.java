package com.example.rentora.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

   // @NotNull(message = "Product ID cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer productId;

   // @NotNull(message = "Renter ID cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer renterId;

    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date must be today or in the future")
    @Column(columnDefinition = "date not null")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @Column(columnDefinition = "date not null")
    private LocalDate endDate;

    @NotEmpty(message = "Status cannot be blank")
    @Column(columnDefinition = "varchar(20) not null")
    private String status;
}
