package com.example.antifraudsystem;

import com.example.antifraudsystem.model.entity.Transaction;
import com.example.antifraudsystem.model.entity.TransactionStatus;
import com.example.antifraudsystem.repository.TransactionStatusRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AntifraudSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AntifraudSystemApplication.class, args);
    }

    @Bean
    public CommandLineRunner transactionStatusPreSetup(TransactionStatusRepository transactionStatusRepository) {
        return args -> {
            if (transactionStatusRepository.count() == 0) {

                transactionStatusRepository.save(new TransactionStatus(
                        com.example.antifraudsystem.util.enums.TransactionStatus.ALLOWED.toString(), 1, 200));

                transactionStatusRepository.save(new TransactionStatus(
                        com.example.antifraudsystem.util.enums.TransactionStatus.MANUAL_PROCESSING.toString(), 201, 1500));

                transactionStatusRepository.save(new TransactionStatus(
                        com.example.antifraudsystem.util.enums.TransactionStatus.PROHIBITED.toString(), 1501, Long.MAX_VALUE));
            }

            Transaction.setAllowedLimit(
                    transactionStatusRepository
                            .findByName(com.example.antifraudsystem.util.enums.TransactionStatus.ALLOWED.toString()).getMax());

            Transaction.setManualLimit(
                    transactionStatusRepository
                            .findByName(com.example.antifraudsystem.util.enums.TransactionStatus.MANUAL_PROCESSING.toString()).getMax());
        };
    }
}
