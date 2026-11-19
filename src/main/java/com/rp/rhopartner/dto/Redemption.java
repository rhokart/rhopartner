package com.rp.rhopartner.dto;

import com.rhokart.datamodel.dto.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Redemption extends ResponseData {

    private Boolean status;
    private String transactionId;
    private String targetAccount;
    private Integer redeemedAmount;
}
