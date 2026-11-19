package com.rp.rhopartner.model;


import com.rhokart.datamodel.dto.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankDetail extends ResponseData {
    private String accountHolderName;
    private String accountNo;
    private String ifscCode;
    private String bankName;
    private String branchName;
    private String branchCityName;
    private String accountType;
    private boolean isIFscVerified;
}
