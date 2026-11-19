package com.rp.rhopartner.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RedeemedDetail {
    private String applicationId;   // This one is needed for tracking purpose.
    private String partnerId;
    private Integer redemptionAmount;

}
