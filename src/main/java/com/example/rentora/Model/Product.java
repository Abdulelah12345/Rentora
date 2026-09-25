package com.example.rentora.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

   // @NotNull(message = "Owner ID cannot be null")
    @Column(columnDefinition = "int not null")
    private Integer ownerId;

    @NotEmpty(message = "Name cannot be blank")
    @Column(columnDefinition = "varchar(100) not null")
    private String name;

    @NotEmpty(message = "Description cannot be blank")
    @Column(columnDefinition = "text not null")
    private String description;

    @NotEmpty(message = "Category cannot be blank")
    @Column(columnDefinition = "varchar(50) not null")
    private String category;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    @Column(columnDefinition = "decimal(10,2) not null")
    private Double pricePerDay;

    @NotNull(message = "Deposit cannot be null")
    @PositiveOrZero(message = "Deposit cannot be negative")
    @Column(columnDefinition = "decimal(10,2) not null")
    private Double deposit;

    @NotNull(message = "Available status cannot be null")
    @Column(columnDefinition = "boolean not null")
    private Boolean available = true;
}