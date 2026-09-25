package com.example.rentora.Repository;

import com.example.rentora.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserRepository extends JpaRepository<User,Integer> {

    User findUserById(Integer id);
    User findUserByEmail(String email);


}
