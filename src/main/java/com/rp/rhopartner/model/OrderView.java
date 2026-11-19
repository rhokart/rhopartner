package com.rp.rhopartner.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderView {

    private String userId;
    private String orderId;
    private String invoiceId;
    private String sellerId;
    private LocalDateTime orderAt;
    private Integer totalAmount;
    private Integer orderAmount;
    private String orderStatus;
    private String paymentStatus;
    private String addressId;
    private String trackingId;
    private LocalDate estimatedDeliveryDate;
    private LocalDateTime deliveredAt;
    private String trackingStatus;
    private String paymentId;
    private String pgStatus;
    private Double rhopartnerCommissionPct;
    private Integer rhokartCommission;
}
