package com.example.rentora.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Name cannot be empty")
    @Column(columnDefinition = "varchar(50) not null")
    private String name;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Must be a valid email")
    @Column(columnDefinition = "varchar(100) not null unique")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(columnDefinition = "varchar(255) not null")
    private String password;

    @NotEmpty(message = "Phone cannot be empty")
    @Size(min = 10, max = 10, message = "Phone must be exactly 10 digits")
    @Column(columnDefinition = "varchar(10) not null")
    private String phone;
}
