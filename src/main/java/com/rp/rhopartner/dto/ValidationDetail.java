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
public class ValidationDetail extends ResponseData {
    private Boolean isRegister;
    private String partnerId;
    private String mobileNo;
    private String email;
    private String accessToken;
    private String refreshToken;

}
