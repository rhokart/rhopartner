package com.rp.rhopartner.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetail {
    private String transactionId;
    private String partnerId;
    private LocalDateTime transactionTimestamp;
    private String transactionType;
    private Integer amount;
    private String buyerName;
    private String invoiceId;
    private String orderId;
    private String targetPaymentId;
    private String description;
    private String message;
    private String insertBy;
    private String updateBy;
    private String transactionStatus;
}
