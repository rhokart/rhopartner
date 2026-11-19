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
public class PincodeDetail extends ResponseData {
    private String pincode;
    private String cityName;
    private String districtName;
    private String state;
    private String country;
}
