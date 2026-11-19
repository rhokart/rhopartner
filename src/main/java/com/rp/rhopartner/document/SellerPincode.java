package com.rp.rhopartner.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "seller_pincode")
public class SellerPincode {
    private String country;
    private String state;
    private String district;
    private String city;
    @Id
    private String pincode;
    private Boolean isActive;
    private List<String> activeSellerIds;
}
