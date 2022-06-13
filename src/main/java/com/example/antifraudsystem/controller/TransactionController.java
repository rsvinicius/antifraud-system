package com.example.antifraudsystem.controller;

import com.example.antifraudsystem.model.View;
import com.example.antifraudsystem.model.entity.Transaction;
import com.example.antifraudsystem.model.request.TransactionFeedbackRequest;
import com.example.antifraudsystem.model.response.TransactionResponse;
import com.example.antifraudsystem.service.TransactionService;
import com.example.antifraudsystem.util.validators.CardNumber;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/antifraud")
@Validated
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transaction")
    public TransactionResponse transaction(@Valid @RequestBody Transaction transaction) {
        return transactionService.transaction(transaction);
    }

    @PutMapping("/transaction")
    @JsonView(View.TransactionView.class)
    public Transaction feedback(@Valid @RequestBody TransactionFeedbackRequest transactionFeedbackRequest) {
        return transactionService.feedback(transactionFeedbackRequest);
    }

    @GetMapping("/history")
    @JsonView(View.TransactionView.class)
    public List<Transaction> listAllTransactions() {
        return transactionService.findAllTransactions();
    }

    @GetMapping("/history/{number}")
    @JsonView(View.TransactionView.class)
    public List<Transaction> listAllTransactionsByCardNumber(@CardNumber @PathVariable String number) {
        return transactionService.findAllTransactionsByCardNumber(number);
    }
}
