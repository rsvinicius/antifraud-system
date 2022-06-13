package com.example.antifraudsystem.repository;

import com.example.antifraudsystem.model.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByNumber(String cardNumber);
    Boolean existsByNumber(String cardNumber);
}
