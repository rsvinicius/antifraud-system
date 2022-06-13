package com.example.antifraudsystem.model.request;

import com.example.antifraudsystem.util.enums.TransactionStatus;
import com.example.antifraudsystem.util.validators.ValueOfEnum;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class TransactionFeedbackRequest {
    @Min(value = 1)
    private long TransactionId;

    @ValueOfEnum(enumClass = TransactionStatus.class)
    private String feedback;
}
