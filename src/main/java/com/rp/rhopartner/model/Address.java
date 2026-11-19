package com.rp.rhopartner.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {
    private String addressLine1;
    private String addressLine2;
    private String pincode;
    private String city;
    private String district;
    private String state;
}
