package com.example.antifraudsystem.model.response;

import lombok.Data;

@Data
public class DeleteCardResponse {
    public DeleteCardResponse(String cardNumber) {
        this.status = String.format("Card %s successfully removed!", cardNumber);
    }

    private String status;
}
