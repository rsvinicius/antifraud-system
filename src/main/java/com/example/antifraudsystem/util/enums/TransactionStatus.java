package com.example.antifraudsystem.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionStatus {
    ALLOWED(1, 200),
    MANUAL_PROCESSING(201, 1500),
    PROHIBITED(1501, Long.MAX_VALUE);

    private final long min;
    private final long max;
}
