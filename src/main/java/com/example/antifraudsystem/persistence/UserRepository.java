package com.example.antifraudsystem.persistence;

import com.example.antifraudsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);

    Boolean existsByUsername(String username);
}

