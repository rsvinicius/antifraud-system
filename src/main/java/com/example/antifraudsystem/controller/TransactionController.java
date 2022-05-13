package com.example.antifraudsystem.controller;

import com.example.antifraudsystem.model.entity.Transaction;
import com.example.antifraudsystem.model.response.TransactionResponse;
import com.example.antifraudsystem.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/antifraud/transaction")
@Validated
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public TransactionResponse transaction(@Valid @RequestBody Transaction transaction) {
        return transactionService.transaction(transaction);
    }
}
