package com.rp.rhopartner.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionInfo {

    private String partnerId;
    private Boolean isActive;
    private String insertBy;
    private String updateBy;
    private Integer lifetimeEarnedIncentiveAmount;
    private Integer redeemedIncentiveAmount;
    private Integer redemptionAvailableAmount;
    private Integer pendingIncentiveAmount;

}
