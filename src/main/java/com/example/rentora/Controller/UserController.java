package com.example.rentora.Controller;

import com.example.rentora.Api.ApiResponse;
import com.example.rentora.Model.User;
import com.example.rentora.Service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllUsers(){
        List<User> users=userService.getAllUsers();
        if(users.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("There is no users to be shown"));
        }
        return ResponseEntity.status(200).body(users);
    }
@PostMapping("/add")
    public ResponseEntity<?> addUsers(@RequestBody @Valid User user , Errors errors){
        if(errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        userService.addUser(user);
        return ResponseEntity.status(200).body(new ApiResponse("User Added Successfully "));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @Valid @RequestBody User user, Errors errors){
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        boolean isUpdated = userService.updateUser(id, user);
        if (!isUpdated) {
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));

        }
        return ResponseEntity.status(200).body(new ApiResponse("User updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        boolean isDeleted = userService.deleteUser(id);
        if (!isDeleted) {
            return ResponseEntity.status(400).body(new ApiResponse("User not found"));

        }
        return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
    }

    @PostMapping("/item-request/{renterId}")
    public ResponseEntity<?> requestNeededItem(@PathVariable Integer renterId, @RequestBody String itemDescription) {


        if (itemDescription == null || itemDescription.isBlank()) {
            return ResponseEntity.status(400).body(new ApiResponse("Item description cannot be empty"));
        }

        int result = userService.requestNeededItem(renterId, itemDescription);

        if (result == 1) {
            return ResponseEntity.status(400).body(new ApiResponse("Renter not found"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Your request has been broadcasted to all users successfully!"));
    }
}
