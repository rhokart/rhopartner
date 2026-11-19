package com.rp.rhopartner.dto;

import com.rhokart.datamodel.dto.ResponseData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDto extends ResponseData {
    private String invoiceNumber;
    private String buyerName;
    private Integer finalInvoiceAmount;
    private String sellerName;
    private String orderDate;
    private String deliveredDate;
    private Integer incentiveAmount;
    private List<OrderedItemDto> orderedItems;
}
