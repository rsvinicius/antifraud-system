package com.example.antifraudsystem.repository;

import com.example.antifraudsystem.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByTransactionId(long id);

    List<Transaction> findAllByNumberAndDateBetween(String cardNumber, LocalDateTime dateFrom, LocalDateTime dateTo);

    List<Transaction> findAllByNumber(String cardNumber);

    long countAllByNumber(String cardNumber);
}
