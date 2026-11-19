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
public class RegisterDetail extends ResponseData {

    private String partnerId;
    private String fname;
    private String lname;
    private String mobile;
    private String email;
    private String accessToken;
    private String refreshToken;
}
