package com.rp.rhopartner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionSummery {
    private Integer lifeTimeIncentive;
    private Integer redeemedAmount;
    private Integer redeemableAmount;
}
