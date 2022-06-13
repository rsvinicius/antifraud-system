package com.example.antifraudsystem.repository;

import com.example.antifraudsystem.model.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionStatusRepository extends JpaRepository<TransactionStatus, Long> {
    TransactionStatus findByName(String name);
}