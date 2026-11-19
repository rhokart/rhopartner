package com.rp.rhopartner.service;

import com.rp.rhopartner.dto.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransaction {

    ValidationResult doValidateId(String id,String partnerId);
    Redemption doRedemptionForPartner(RedeemedDetail redeemedDetail);
    PartnerTransactionDetail getTransactionDetailForPartner(String partnerId,String startDate, String endDate);
    ValidationResult doValidateQr(String qr, String partnerId);
    OrderDetailDto getOrderDetail(String orderId);
}
