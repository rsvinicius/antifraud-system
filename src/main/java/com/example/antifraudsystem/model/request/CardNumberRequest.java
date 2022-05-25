package com.example.antifraudsystem.model.request;

import com.example.antifraudsystem.util.validators.CardNumber;
import lombok.Data;

@Data
public class CardNumberRequest {
    @CardNumber
    private String number;
}