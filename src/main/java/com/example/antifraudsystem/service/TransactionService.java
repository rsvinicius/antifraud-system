package com.example.antifraudsystem.service;

import com.example.antifraudsystem.model.Transaction;
import com.example.antifraudsystem.model.response.TransactionResponse;
import com.example.antifraudsystem.util.enums.TransactionResultType;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    public TransactionResponse transaction(Transaction transaction) {
        TransactionResultType transactionResultType;
        Long amount = transaction.getAmount();

        if (amount <= 200) {
            transactionResultType = TransactionResultType.ALLOWED;
        } else if (amount > 1500) {
            transactionResultType = TransactionResultType.PROHIBITED;
        } else {
            transactionResultType = TransactionResultType.MANUAL_PROCESSING;
        }

        return new TransactionResponse(transactionResultType);
    }
}

