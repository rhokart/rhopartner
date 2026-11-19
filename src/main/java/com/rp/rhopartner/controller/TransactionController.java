package com.rp.rhopartner.controller;

import com.rhokart.datamodel.dto.*;
import com.rhokart.datamodel.util.Constants;
import com.rp.rhopartner.dto.OrderDetailDto;
import com.rp.rhopartner.dto.RedeemedDetail;
import com.rp.rhopartner.dto.Redemption;
import com.rp.rhopartner.service.ITransaction;
import com.rp.rhopartner.util.GlobalConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private ITransaction transaction;

    @PostMapping("/redeemed")
    ResponseEntity<Response> redeemedWallet(@RequestBody RedeemedDetail redeemedDetail){
        Redemption redemption = transaction.doRedemptionForPartner(redeemedDetail);

        if(!redemption.getStatus())
            return ResponseEntity.ok(Response.builder()
                            .status(Constants.STATUS_FAIL)
                            .failure(FailureInfo.builder()
                                    .errCode(GlobalConstants.ERROR_CODE)
                                    .errStatus(GlobalConstants.ERROR_STATUS)
                                    .errMessage("Redeemed does not happened...")
                                    .errDescription("Error while redemption.. ")
                                    .build())
                    .build());


        return ResponseEntity.ok(Response.builder().status(Constants.STATUS_SUCCESS)
                        .success(SuccessInfo.builder()
                                .successCode(Constants.SUCCESS_CODE)
                                .execTimestamp(Constants.getCurrentTimestamp())
                                .coreData(CoreData.builder().responseData(redemption).build())
                                .build())
                .build());
    }

    @GetMapping("/info")
    ResponseEntity<Response> getTransactionDetail(@RequestParam("partnerId") String partnerId,
                                                  @RequestParam(value = "startDate",required = false) String startDate,
                                                  @RequestParam(value = "endDate",required = false) String endDate
                                                  ){

        return ResponseEntity.ok(Response.builder()
                        .status(Constants.STATUS_SUCCESS)
                        .success(SuccessInfo.builder()
                                .execTimestamp(Constants.getCurrentTimestamp())
                                .successCode(Constants.SUCCESS_CODE)
                                .coreData(CoreData.builder()
                                        .responseData(transaction.getTransactionDetailForPartner(partnerId, startDate, endDate))
                                        .build())
                                .build())
                .build());
    }

    @GetMapping("/order/detail")
    ResponseEntity<Response> getOrderDetail(@RequestParam("orderId") String orderId){
        return ResponseEntity.ok(Response.builder()
                .status(Constants.STATUS_SUCCESS)
                .success(SuccessInfo.builder()
                        .execTimestamp(Constants.getCurrentTimestamp())
                        .successCode(Constants.SUCCESS_CODE)
                        .coreData(CoreData.builder()
                                .responseData(transaction.getOrderDetail(orderId))
                                .build())
                        .build())
                .build());
    }
}
