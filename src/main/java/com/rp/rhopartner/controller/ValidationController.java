package com.rp.rhopartner.controller;

import com.rhokart.datamodel.dto.CoreData;
import com.rhokart.datamodel.dto.FailureInfo;
import com.rhokart.datamodel.dto.Response;
import com.rhokart.datamodel.dto.SuccessInfo;
import com.rhokart.datamodel.util.Constants;
import com.rp.rhopartner.dto.ValidationResult;
import com.rp.rhopartner.model.BankDetail;
import com.rp.rhopartner.service.AutoFetch;
import com.rp.rhopartner.service.ITransaction;
import com.rp.rhopartner.util.GlobalConstants;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/validation")
public class ValidationController {

    @Autowired
    private ITransaction iTransaction;

    private final AutoFetch autoFetchService;

    public ValidationController(AutoFetch autoFetchService) {
        this.autoFetchService = autoFetchService;
    }

    @GetMapping("/qr")
    public ResponseEntity<Response> processQr(@Valid @RequestParam("partnerId") String partnerId, @Valid @RequestParam("qr") String qr){
        ValidationResult result = iTransaction.doValidateQr(qr,partnerId);

//        if(Objects.isNull(result.getOrderId())){
//            return ResponseEntity.ok(Response.builder()
//                    .status(Constants.STATUS_FAIL)
//                    .failure(FailureInfo.builder()
//                            .errMessage("QR validation failed.")
//                            .errDescription("QR Validation failed, please correct and try again.")
//                            .errCode(GlobalConstants.ERROR_CODE)
//                            .errStatus(GlobalConstants.ERROR_STATUS)
//                            .build())
//                    .build());
//
//        }
        return ResponseEntity.ok(Response.builder()
                .status(Constants.STATUS_SUCCESS)
                .success(SuccessInfo.builder()
                        .execTimestamp(Constants.getCurrentTimestamp())
                        .successCode(Constants.SUCCESS_CODE)
                        .coreData(CoreData.builder()
                                .responseData(result).build())
                        .build())
                .build());

    }

    @GetMapping("/id")
    public ResponseEntity<Response> processInvoices(@Valid @RequestParam("partnerId") String partnerId, @Valid @RequestParam("id") String id){

        ValidationResult result = iTransaction.doValidateId(id,partnerId);

//        if(Objects.isNull(result.getOrderId())){
//            return ResponseEntity.ok(Response.builder()
//                            .status(Constants.STATUS_FAIL)
//                            .failure(FailureInfo.builder()
//                                    .errMessage("Invoice or Order validation failed.")
//                                    .errDescription("Validation failed, please correct and try again.")
//                                    .errCode(GlobalConstants.ERROR_CODE)
//                                    .errStatus(GlobalConstants.ERROR_STATUS)
//                                    .build())
//                            .build());
//
//        }
        return ResponseEntity.ok(Response.builder()
                        .status(Constants.STATUS_SUCCESS)
                        .success(SuccessInfo.builder()
                                .execTimestamp(Constants.getCurrentTimestamp())
                                .successCode(Constants.SUCCESS_CODE)
                                .coreData(CoreData.builder()
                                        .responseData(result).build())     // generate the respose for id and partnerId.
                                        .build())
                                .build());
    }

    @GetMapping("/fetch-bank-detail")
    @SneakyThrows
    public ResponseEntity<Response> fetchBankDetail(@RequestParam("ifsc") String ifsc) {
        if (ifsc == null || ifsc.length() < 11) {
            return ResponseEntity.ok(Response.builder()
                    .status(Constants.STATUS_FAIL)
                    .failure(FailureInfo.builder()
                            .errMessage("Valid IFSC code is required")
                            .errDescription("Valid IFSC code is required, length should be less than 11 characters")
                            .errCode(GlobalConstants.ERROR_CODE)
                            .errStatus(GlobalConstants.ERROR_STATUS)
                            .build())
                    .build());
        }
        BankDetail bankDetail = autoFetchService.fetchBankDetail(ifsc);

        if (bankDetail.isIFscVerified()) {
            return ResponseEntity.ok(Response.builder()
                    .status(Constants.STATUS_SUCCESS)
                    .success(SuccessInfo.builder()
                            .execTimestamp(Constants.getCurrentTimestamp())
                            .successCode(Constants.SUCCESS_CODE)
                            .coreData(CoreData.builder()
                                    .responseData(bankDetail).build())
                            .build())
                    .build());

        }
        return ResponseEntity.ok(Response.builder()
                .status(Constants.STATUS_FAIL)
                .failure(FailureInfo.builder()
                        .errMessage("Error fetching bank details")
                        .errDescription("Error fetching bank details, please try again")
                        .errCode(GlobalConstants.ERROR_CODE)
                        .errStatus(GlobalConstants.ERROR_STATUS)
                        .build())
                .build());
    }

    @GetMapping("/pincode/detail")
    @SneakyThrows
    ResponseEntity<Response> getPincodeDetail(@RequestParam("pincode") String pincode){
        return ResponseEntity.ok(Response.builder()
                .status(Constants.STATUS_SUCCESS)
                .success(SuccessInfo.builder()
                        .execTimestamp(Constants.getCurrentTimestamp())
                        .successCode(Constants.SUCCESS_CODE)
                        .coreData(CoreData.builder()
                                .responseData(autoFetchService.fetchLocationDetailByPincod(pincode)).build())
                        .build())
                .build());

    }

}
