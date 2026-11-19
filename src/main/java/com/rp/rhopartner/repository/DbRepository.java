package com.rp.rhopartner.repository;

import com.rp.rhopartner.dto.OrderDetailDto;
import com.rp.rhopartner.dto.OrderedItemDto;
import com.rp.rhopartner.model.OrderView;
import com.rp.rhopartner.model.PartnerInfo;
import com.rp.rhopartner.model.TransactionDetail;
import com.rp.rhopartner.model.TransactionInfo;
import com.rp.rhopartner.util.DbMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@DbMapper
public interface DbRepository {

    @Select(SqlQuery.SELECT_PARTNER_INFO)
    @Results({
            @Result(property = "partnerId",column = "partner_id"),
            @Result(property = "fname",column = "fname"),
            @Result(property = "lname",column = "lname"),
            @Result(property = "mobileNo",column = "mob"),
            @Result(property = "email",column = "email")
    })
    PartnerInfo selectPartNerInfoByMobile(@Param("mobileNo") String mobileNo);

    @Select(SqlQuery.SELECT_PARTNER_INFO_BY_EMAIL)
    @Results({
            @Result(property = "partnerId",column = "partner_id"),
            @Result(property = "fname",column = "fname"),
            @Result(property = "lname",column = "lname"),
            @Result(property = "mobileNo",column = "mob"),
            @Result(property = "email",column = "email")
    })
    PartnerInfo selectPartNerInfoByEmail(@Param("email") String email);

    @Select(SqlQuery.SELECT_PARTNER_INFO_BY_PARTNERID)
    @Results({
            @Result(property = "partnerId",column = "partner_id"),
            @Result(property = "fname",column = "fname"),
            @Result(property = "lname",column = "lname"),
            @Result(property = "mobileNo",column = "mob"),
            @Result(property = "altMobileNo",column = "alt_mob"),
            @Result(property = "email",column = "email"),
            @Result(property = "dob",column = "dob"),
            @Result(property = "address",column = "address"),
            @Result(property = "pinCode",column = "pincode"),
            @Result(property = "district",column = "district"),
            @Result(property = "city",column = "city"),
            @Result(property = "state",column = "state"),
            @Result(property = "upiId",column = "upi_id"),
            @Result(property = "accType",column = "acc_type"),
            @Result(property = "accNumber",column = "acc_number"),
            @Result(property = "ifscCode",column = "ifsc_code"),
            @Result(property = "bankName",column = "bank_name"),
            @Result(property = "branchName",column = "branch_name"),
            @Result(property = "accHolderName",column = "acc_holder_name"),
            @Result(property = "panNumber",column = "pan_number"),
            @Result(property = "panUrl",column = "pan_url"),
            @Result(property = "aadharNumber",column = "aadhar_nubmer"),
            @Result(property = "aadharUrl",column = "aadhar_url"),
    })
    PartnerInfo fetchPartNerInfoByPartnerId(@Param("partnerId") String partnerId);


    @Insert(SqlQuery.INSERT_PARTNER_INFO)
    void insertPartnerInfoToDb(PartnerInfo partnerInfo);

    @Select(SqlQuery.SELECT_TRANSACTION_INFO)
    @Results({
            @Result(property = "partnerId",column = "partner_id"),
            @Result(property = "lifetimeEarnedIncentiveAmount",column = "lifetime_earned_incentive_amt"),
            @Result(property = "redeemedIncentiveAmount",column = "redeemed_incentive_amt"),
            @Result(property = "redemptionAvailableAmount",column = "redemption_available_amt"),
            @Result(property = "pendingIncentiveAmount",column = "pending_incentive_amt"),
            @Result(property = "isActive",column = "is_active")
    })
    TransactionInfo getTransactionInfoFromDb(@Param("partnerId") String partnerId);

    @Insert(SqlQuery.INSERT_TRANSACTION_INFO)
    void saveTransactionInfoToDb(TransactionInfo transactionInfo);


    @Select(SqlQuery.SELECT_ORDER_VIEW)
    @Results({
            @Result(property = "userId",column = "user_id"),
            @Result(property = "orderId",column = "ord_id"),
            @Result(property = "invoiceId",column = "invoice_id"),
            @Result(property = "sellerId",column = "seller_id"),
            @Result(property = "orderAt",column = "order_at"),
            @Result(property = "totalAmount",column = "total_amt"),
            @Result(property = "orderAmount",column = "order_amt"),
            @Result(property = "orderStatus",column = "order_status"),
            @Result(property = "paymentStatus",column = "pay_status"),
            @Result(property = "addressId",column = "addr_id"),
            @Result(property = "trackingId",column = "trk_id"),
            @Result(property = "estimatedDeliveryDate",column = "est_delivery_dte"),
            @Result(property = "deliveredAt",column = "delivered_at"),
            @Result(property = "trackingStatus",column = "track_status"),
            @Result(property = "paymentId",column = "pay_id"),
            @Result(property = "pgStatus",column = "pg_status"),
            @Result(property = "rhopartnerCommissionPct",column = "rhopartner_commission_pct"),
            @Result(property = "rhokartCommission",column = "rhokart_commission")
    })
    List<OrderView> getOrderViewFromDb(@Param("invoiceId") String invoiceId, @Param("orderId") String orderId);

    @Insert(SqlQuery.INSERT_TRANSACTION_DETAIL)
    void storeTransactionDetailOnDb(TransactionDetail transactionDetail);

    @Update(SqlQuery.UPDATE_TRANSACTION_INFO)
    void updateTransactionInfoWithAmounts(@Param("commissionAmount") Integer commissionAmount,
                                          @Param("partnerId") String partnerId);

    @Update(SqlQuery.UPDATE_PENDING_INCENTIVE_AMT)
    void updatePendingIncentiveAmount(@Param("commissionAmount") Integer commissionAmount, @Param("partnerId") String partnerId);

    @Select(SqlQuery.GET_TRANSACTION_DTL_CNT)
    Integer getTransactionDetailCountByOrderId(@Param("orderId") String orderId);

    @Update(SqlQuery.UPDATE_TRANSACTION_INFO_REDEEMED)
    void updateTransactionInfoWithRedeem(@Param("redeemedAmount") Integer redeemedAmount, @Param("partnerId") String partnerId);

    @Select(SqlQuery.FETCH_TRANSACTION_DETAIL_PARTNERID)
    @Results({
            @Result(property = "transactionId",column = "tran_id"),
            @Result(property = "partnerId",column = "partner_id"),
            @Result(property = "transactionTimestamp",column = "tran_timestamp"),
            @Result(property = "transactionType",column = "tran_type"),
            @Result(property = "amount",column = "amount"),
            @Result(property = "buyerName",column = "buyer_name"),
            @Result(property = "invoiceId",column = "invoice_id"),
            @Result(property = "orderId",column = "order_id"),
            @Result(property = "targetPaymentId",column = "target_payment_id"),
            @Result(property = "description",column = "description"),
            @Result(property = "message",column = "message"),
            @Result(property = "insertBy",column = "insert_by"),
            @Result(property = "updateBy",column = "update_by"),
            @Result(property = "transactionStatus",column = "tran_status"),
    })
    List<TransactionDetail> fetchTransactionDetailsByPartnerIdFromDb(@Param("partnerId") String partnerId);

    @Select(SqlQuery.FETCH_TRANSACTION_DETAIL_PARTNERID_WITH_DATE)
    @Results({
            @Result(property = "transactionId",column = "tran_id"),
            @Result(property = "partnerId",column = "partner_id"),
            @Result(property = "transactionTimestamp",column = "tran_timestamp"),
            @Result(property = "transactionType",column = "tran_type"),
            @Result(property = "amount",column = "amount"),
            @Result(property = "buyerName",column = "buyer_name"),
            @Result(property = "invoiceId",column = "invoice_id"),
            @Result(property = "orderId",column = "order_id"),
            @Result(property = "targetPaymentId",column = "target_payment_id"),
            @Result(property = "description",column = "description"),
            @Result(property = "message",column = "message"),
            @Result(property = "insertBy",column = "insert_by"),
            @Result(property = "updateBy",column = "update_by"),
            @Result(property = "transactionStatus",column = "tran_status"),
    })
    List<TransactionDetail> fetchTransactionDetailsByPartnerAndDateRange(
            @Param("partnerId") String partnerId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    @Update(SqlQuery.UPDATE_PARTNER_DETAIL)
    void updatePartnerDetail(@Param("partnerId") String partnerId,
                             @Param("fname") String fname,
                             @Param("lname") String lname,
                             @Param("mob") String mob,
                             @Param("altMob") String altMob,
                             @Param("email") String email,
                             @Param("dob") String dob,
                             @Param("address") String address,
                             @Param("pincode") String pincode,
                             @Param("district") String district,
                             @Param("city") String city,
                             @Param("state") String state,
                             @Param("upiId") String upiId,
                             @Param("accHolderName") String accHolderName,
                             @Param("accType") String accType,
                             @Param("accNumber") String accNumber,
                             @Param("ifscCode") String ifscCode,
                             @Param("bankName") String bankName,
                             @Param("branchName") String branchName,
                             @Param("panNumber") String panNumber,
                             @Param("panUrl") String panUrl,
                             @Param("aadharNumber") String aadharNumber,
                             @Param("aadharUrl") String aadharUrl);

    @Select(SqlQuery.SELECT_ORDER_DETAIL_FROM_VW_ORDER_FULLFILL)
    @Results({
            @Result(property = "invoiceNumber", column = "invoice_id"),
            @Result(property = "buyerName", column = "fname"),
            @Result(property = "finalInvoiceAmount", column = "total_amt"),
            @Result(property = "orderDate", column = "order_at"),
            @Result(property = "deliveredDate", column = "update_tmstp")
    })
    OrderDetailDto getOrderDetail(@Param("orderId") String orderId);

    @Select(SqlQuery.SELECT_ORDERED_ITEM_DETAIL_FROM_ORDER_DETAIL)
    @Results({
            @Result(property = "quantity", column = "quantity"),
            @Result(property = "skuId", column = "sku_id"),
            @Result(property = "skuName", column = "sku_nme"),
            @Result(property = "totalAmount", column = "total_amt")
    })
    List<OrderedItemDto> getOrderedItemDetail(@Param("orderId") String orderId);

    @Select(SqlQuery.SELECT_ORDER_DETAILS)
    @Results({
            @Result(property = "userId",column = "user_id"),
            @Result(property = "orderId",column = "ord_id"),
            @Result(property = "sellerId",column = "seller_id"),
            @Result(property = "invoiceId",column = "invoice_id"),
            @Result(property = "orderAt",column = "order_at"),
            @Result(property = "totalAmount",column = "total_amt"),
            @Result(property = "deliveredAt",column = "delivered_at")
    })
    List<OrderView> getOrderDetails(@Param("orderId") String orderId);
}
