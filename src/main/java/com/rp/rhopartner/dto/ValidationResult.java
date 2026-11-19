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
public class ValidationResult extends ResponseData {

    private String invoiceNumber;
    private String orderId;
    private String buyerName;
    private Integer invoiceAmount;
    private String orderDate;
    private String deliverDate;
    private Boolean isDeliver;
    private Integer incentiveAmount;
    private Boolean isQrValid;
    private String message;
}
