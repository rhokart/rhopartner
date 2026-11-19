package com.rp.rhopartner.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDetail {
    private String businessName;
    private String gstNumber;
    private String businessPan;
}
