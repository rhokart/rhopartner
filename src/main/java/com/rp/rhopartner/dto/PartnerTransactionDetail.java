package com.rp.rhopartner.dto;

import com.rhokart.datamodel.dto.ResponseData;
import com.rp.rhopartner.model.TransactionDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartnerTransactionDetail extends ResponseData {
    private String partnerId;
    private String fname;
    private String lname;
    private String targetAccount;
    private TransactionSummery transactionSummery;
    private List<TransactionDetail> transactionDetail;

}
