package com.example.antifraudsystem.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionStatus {
    ALLOWED,
    MANUAL_PROCESSING,
    PROHIBITED;
}