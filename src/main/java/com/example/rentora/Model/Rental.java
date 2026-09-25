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
    public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

   // @NotNull(message = "Request ID cannot be null")
    @Column(columnDefinition = "int not null unique")
    private Integer requestId;

    @NotNull(message = "Start date cannot be null")
    @Column(columnDefinition = "date not null")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @Column(columnDefinition = "date not null")
    private LocalDate endDate;

    @NotBlank(message = "Status cannot be blank")
    @Pattern(regexp = "AWAITING_PAYMENT|ACTIVE|COMPLETED|LATE|CANCELLED", message = "Invalid status value")
    @Column(columnDefinition = "varchar(20) not null")
    private String status;

    @NotNull(message = "Total price cannot be null")
    @PositiveOrZero(message = "Total price cannot be negative")
    @Column(columnDefinition = "decimal(10,2) not null")
    private Double totalPrice;

    @NotNull(message = "Late fee cannot be null")
    @PositiveOrZero(message = "Late fee cannot be negative")
    @Column(columnDefinition = "decimal(10,2) not null")
    private Double lateFee = 0.0;
    }
