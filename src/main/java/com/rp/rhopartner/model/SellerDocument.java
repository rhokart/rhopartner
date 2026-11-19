package com.rp.rhopartner.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "seller_detail")
public class SellerDocument {

    @Id
    private String id;
    private String sellerName;
    private String email;
    private String mobileNo;
}
