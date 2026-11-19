package com.rp.rhopartner.dto;

import lombok.Data;

@Data
public class OrderedItemDto {
    private Integer quantity;
    private String skuId;
    private String skuName;
    private Integer totalAmount;
}
