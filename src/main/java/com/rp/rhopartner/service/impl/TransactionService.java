package com.rp.rhopartner.service.impl;

import com.rp.rhopartner.dto.*;
import com.rp.rhopartner.model.*;
import com.rp.rhopartner.repository.DbRepository;
import com.rp.rhopartner.repository.SellerRepository;
import com.rp.rhopartner.repository.UserRepository;
import com.rp.rhopartner.service.ITransaction;
import com.rp.rhopartner.util.GlobalConstants;
import com.rp.rhopartner.util.PartnerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class TransactionService implements ITransaction {

    @Autowired
    private DbRepository dbRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public ValidationResult doValidateId(String id, String partnerId) {
        Boolean isDeliver = Boolean.FALSE;
        String deliveryDate = GlobalConstants.EMPTY_STRING;
        String buyerName = GlobalConstants.EMPTY_STRING;
        Integer commissionAmount;
        List<OrderView> orderViews;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // 1. Validate partnerId
        if(isValidPartnerId(partnerId)){
            // 2. Validate invoice or order id.
            orderViews = dbRepository.getOrderViewFromDb(id,id);    // it's a or condition , such either invoice or order id will match.
            if(orderViews.size() != GlobalConstants.ONE){
                log.info("Invoice or order number is not found.");
                return ValidationResult.builder().isQrValid(Boolean.FALSE).message("Invalid invoice ID or order number").build();
            }
            if(!isValidOrderView(orderViews.get(GlobalConstants.ZERO))){
                log.info("Order view validation failed..");
                return ValidationResult.builder().isQrValid(Boolean.FALSE).message("ID maybe already claimed").build();// return empty response.
            }
            //3. Calculate partner commission.
            commissionAmount = (int) ((orderViews.get(GlobalConstants.ZERO).getRhokartCommission() * orderViews.get(0).getRhopartnerCommissionPct())/100);  // casting to integer.
            // 4. Build response and return.
            if(!Objects.isNull(orderViews.get(GlobalConstants.ZERO).getDeliveredAt())){
                isDeliver = Boolean.TRUE;
                deliveryDate = orderViews.get(GlobalConstants.ZERO).getDeliveredAt().format(dateTimeFormatter);
            }
            Optional<User> buyer = userRepository.findById(orderViews.get(GlobalConstants.ZERO).getUserId());
            if(buyer.isPresent())
                buyerName = buyer.get().getFirstName() + " " + buyer.get().getLastName();

            LocalDateTime currentDateTime = LocalDateTime.now();   // current date and time.

            int diff = -1;
            if(!Objects.isNull(orderViews.get(GlobalConstants.ZERO).getDeliveredAt())){
                diff = currentDateTime.compareTo(orderViews.get(GlobalConstants.ZERO).getDeliveredAt().plusDays(GlobalConstants.SEVEN_DAYS));
            }
            // Transaction details table updated with amount.
            dbRepository.storeTransactionDetailOnDb(transactionDetailMapper(partnerId,orderViews.get(GlobalConstants.ZERO),diff,commissionAmount,buyerName));
            // transaction info updated depending on the delivery and encash logic.
            if (diff >=0){
                log.info("update redeemed amount ");
                dbRepository.updateTransactionInfoWithAmounts(commissionAmount,partnerId);
            }else{
                log.info("update pending incentive amounts.");
                dbRepository.updatePendingIncentiveAmount(commissionAmount,partnerId);
            }
            LocalDateTime  localDateTime = orderViews.get(GlobalConstants.ZERO).getOrderAt();
            log.info("date&time:{}",localDateTime.format(dateTimeFormatter));
            return ValidationResult.builder()
                    .orderId(orderViews.get(GlobalConstants.ZERO).getOrderId())
                    .buyerName(buyerName)
                    .incentiveAmount(commissionAmount)
                    .invoiceAmount(orderViews.get(GlobalConstants.ZERO).getTotalAmount())
                    .invoiceNumber(orderViews.get(GlobalConstants.ZERO).getInvoiceId())
                    .isDeliver(isDeliver)
                    .deliverDate(deliveryDate)
                    .isQrValid(Boolean.TRUE)
                    .orderDate(localDateTime.format(dateTimeFormatter))
                    .build();
        }
         return ValidationResult.builder().build();
    }

    @Override
    public Redemption doRedemptionForPartner(RedeemedDetail redeemedDetail) {
        TransactionInfo transactionInfo = dbRepository.getTransactionInfoFromDb(redeemedDetail.getPartnerId());
        if(redeemedDetail.getRedemptionAmount() > transactionInfo.getRedemptionAvailableAmount()){
            log.info("Redemption amount is greater than available amounts ");
            return Redemption.builder().status(Boolean.FALSE).build();
        }
        PartnerInfo partnerInfo = dbRepository.fetchPartNerInfoByPartnerId(redeemedDetail.getPartnerId());
        log.info("Target accounts , redemption will be send :{}",partnerInfo.getUpiId());

        /***
         *  Call the banking process for sending money to target accounts.
         */
        String generatedTransactionId = String.valueOf(Instant.now().toEpochMilli());
        if(!updateTransactionDetail(redeemedDetail, generatedTransactionId))
            return Redemption.builder().status(Boolean.FALSE).build();

        return Redemption.builder().targetAccount("testing@upi").transactionId(generatedTransactionId).status(Boolean.TRUE).redeemedAmount(redeemedDetail.getRedemptionAmount()).build();
    }

    @Override
    public PartnerTransactionDetail getTransactionDetailForPartner(String partnerId,String startDate, String endDate) {
        PartnerInfo partner = dbRepository.fetchPartNerInfoByPartnerId(partnerId);
        String targetAccount ;
        String bankName = partner.getBankName().length() >= 5 ? partner.getBankName().substring(0, 5) : partner.getBankName();

        if(Objects.isNull(partner.getUpiId())) {
            targetAccount = bankName.concat(" XX") + partner.getAccNumber().substring(partner.getAccNumber().length() - 2);
        } else
            targetAccount = partner.getUpiId();

        TransactionInfo info = dbRepository.getTransactionInfoFromDb(partnerId);

        if(Objects.isNull(info)){
            return PartnerTransactionDetail.builder()
                    .fname(partner.getFname())
                    .lname(partner.getLname())
                    .partnerId(partner.getPartnerId())
                    .targetAccount(targetAccount)
                    .transactionDetail(null)
                    .transactionSummery(TransactionSummery.builder()
                            .build())
                    .build();
        }

        List<TransactionDetail> details;
        if(Objects.nonNull(startDate) && Objects.nonNull(endDate)){
            details = dbRepository.fetchTransactionDetailsByPartnerAndDateRange(partnerId, LocalDate.parse(startDate), LocalDate.parse(endDate));
        }else {
            details = dbRepository.fetchTransactionDetailsByPartnerIdFromDb(partnerId);
        }

        return PartnerTransactionDetail.builder()
                .fname(partner.getFname())
                .lname(partner.getLname())
                .partnerId(partner.getPartnerId())
                .targetAccount(targetAccount)
                .transactionDetail(details)
                .transactionSummery(TransactionSummery.builder()
                        .lifeTimeIncentive(info.getLifetimeEarnedIncentiveAmount())
                        .redeemableAmount(info.getRedemptionAvailableAmount())
                        .redeemedAmount(info.getRedeemedIncentiveAmount())
                        .build())
                .build();

    }

    @Override
    public ValidationResult doValidateQr(String qr, String partnerId) {
        String invoiceId = PartnerUtils.getInvoiceIdFromToken(qr);
        log.info("QR validation is done for invoiceId:{}",invoiceId);
        return doValidateId(invoiceId,partnerId);

    }

    @Override
    public OrderDetailDto getOrderDetail(String orderId) {
        List<OrderedItemDto> orderedItems = dbRepository.getOrderedItemDetail(orderId);
        List<OrderView> orderViews = dbRepository.getOrderDetails(orderId);
        if(orderViews.isEmpty()){
            log.info("Order details not found for orderId:{}",orderId);
            return OrderDetailDto.builder().build();
        }
        Optional<User> user = userRepository.findById(orderViews.get(GlobalConstants.ZERO).getUserId());
        SellerDocument seller = sellerRepository.findById(orderViews.get(GlobalConstants.ZERO).getSellerId()).orElse(null);

        assert seller != null;
        return OrderDetailDto.builder()
                .invoiceNumber(orderViews.get(GlobalConstants.ZERO).getInvoiceId())
                .buyerName(user.map(value -> value.getFirstName() + " " + value.getLastName()).orElse(GlobalConstants.EMPTY_STRING))
                .finalInvoiceAmount(orderViews.get(GlobalConstants.ZERO).getTotalAmount())
                .sellerName(seller.getSellerName())
                .orderDate(orderViews.get(GlobalConstants.ZERO).getOrderAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .deliveredDate(orderViews.get(GlobalConstants.ZERO).getDeliveredAt() != null ? orderViews.get(GlobalConstants.ZERO).getDeliveredAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) : GlobalConstants.EMPTY_STRING)
                .incentiveAmount(orderViews.get(GlobalConstants.ZERO).getRhopartnerCommissionPct() != null ? (int) ((orderViews.get(GlobalConstants.ZERO).getRhokartCommission() * orderViews.get(0).getRhopartnerCommissionPct())/100) : GlobalConstants.ZERO)
                .orderedItems(orderedItems)
                .build();
    }

    private Boolean updateTransactionDetail(RedeemedDetail redeemedDetail, String generatedTransactionId){
        TransactionDetail detail = TransactionDetail.builder()
                .updateBy(GlobalConstants.SYSUSER)
                .insertBy(GlobalConstants.SYSUSER)
                .targetPaymentId("testing@upi")
                .transactionId(generatedTransactionId)
                .transactionType(GlobalConstants.REDEEMED)
                .transactionStatus(GlobalConstants.PAID)
                .partnerId(redeemedDetail.getPartnerId())
                .orderId(GlobalConstants.EMPTY_STRING)
                .message("Redeemed is successful.")
                .description("Redeemed is done, amount send to target account.")
                .invoiceId(GlobalConstants.EMPTY_STRING)
                .buyerName(GlobalConstants.EMPTY_VALUE)
                .amount(redeemedDetail.getRedemptionAmount())
                .build();

 //       try {
            dbRepository.storeTransactionDetailOnDb(detail);
            dbRepository.updateTransactionInfoWithRedeem(redeemedDetail.getRedemptionAmount(), redeemedDetail.getPartnerId());
//        } catch (Exception e) {
//            log.info("exceptions occurs while storing in db...");
//            return Boolean.FALSE;
//        }
       return Boolean.TRUE;
    }


    private TransactionDetail transactionDetailMapper(String partnerId, OrderView orderView, int daysdiff, int commissionAmt,String buyerName){
        String transactionStatus = GlobalConstants.EARNED;
        if(daysdiff < GlobalConstants.ZERO)
            transactionStatus =  GlobalConstants.PENDING;

        return TransactionDetail.builder()
                .amount(commissionAmt)
                .buyerName(buyerName)
                .partnerId(partnerId)
                .invoiceId(orderView.getInvoiceId())
                .transactionId(String.valueOf(Instant.now().toEpochMilli()))
                .transactionStatus(transactionStatus)
                .transactionType(GlobalConstants.EARNED)
                .description(GlobalConstants.EARNED_DESC)
                .message(GlobalConstants.EARNED_MSG)
                .insertBy(GlobalConstants.SYSUSER)
                .targetPaymentId(GlobalConstants.EMPTY_STRING)
                .updateBy(GlobalConstants.SYSUSER)
                .orderId(orderView.getOrderId())
                .build();
    }

    private Boolean isValidOrderView(OrderView orderView){
        log.info("validation of order ::{}",orderView);
        if(!GlobalConstants.VALID_ORDER_STATUS.contains(orderView.getOrderStatus().toUpperCase().trim()))
            return Boolean.FALSE;

        if(!orderView.getPaymentStatus().trim().equals("PAID"))
            return Boolean.FALSE;

        if(!Objects.equals(dbRepository.getTransactionDetailCountByOrderId(orderView.getOrderId().trim()), GlobalConstants.ZERO)){
            log.info("order is already validated by others:{}",orderView.getOrderId());
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    private Boolean isValidPartnerId(String partnerId){
        PartnerInfo info = dbRepository.fetchPartNerInfoByPartnerId(partnerId);
        if(Objects.isNull(info) || Objects.isNull(info.getMobileNo()))   // partner id is not present.
            return Boolean.FALSE;

        TransactionInfo transactionInfo = dbRepository.getTransactionInfoFromDb(partnerId);
        if(Objects.isNull(transactionInfo)){
            //first tie entry , we can create 1st transaction.
            log.info("First time transactions, so partner account will be open for transactions.");
            dbRepository.saveTransactionInfoToDb(transactionInfoMapper(info));
            return Boolean.TRUE;
        }
        if(!transactionInfo.getIsActive())     // partner id is inactive in system.
            return Boolean.FALSE;

        return Boolean.TRUE;
    }

    private TransactionInfo transactionInfoMapper(PartnerInfo info){
        return TransactionInfo.builder()
                .partnerId(info.getPartnerId())
                .isActive(Boolean.TRUE)
                .insertBy(GlobalConstants.ADMIN)
                .updateBy(GlobalConstants.ADMIN)
                .lifetimeEarnedIncentiveAmount(GlobalConstants.ZERO)
                .pendingIncentiveAmount(GlobalConstants.ZERO)
                .redeemedIncentiveAmount(GlobalConstants.ZERO)
                .redemptionAvailableAmount(GlobalConstants.ZERO)
                .build();


    }
}
