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
public class Partner {
    private String fname;
    private String lname;
    private String mobileNo;
    private String altMobileNo;
    private String email;
    private String dob;
    private String address;
    private String pinCode;
    private String district;
    private String city;
    private String state;
    private String upiId;
    private String accHolderName;
    private String accType;
    private String accNumber;
    private String ifscCode;
    private String bankName;
    private String branchName;
    private String panNumber;
    private String panUrl;
    private String aadharNumber;
    private String aadharUrl;
}
