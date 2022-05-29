package com.example.antifraudsystem.persistence;

import com.example.antifraudsystem.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByNumberAndDateBetween(String cardNumber, LocalDateTime dateFrom, LocalDateTime dateTo);
}
