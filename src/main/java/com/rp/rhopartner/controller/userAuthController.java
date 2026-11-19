package com.rp.rhopartner.controller;

import com.rhokart.datamodel.dto.*;
import com.rhokart.datamodel.util.Constants;
import com.rp.rhopartner.dto.*;
import com.rp.rhopartner.service.IEmailService;
import com.rp.rhopartner.service.IUserService;
import com.rp.rhopartner.util.GlobalConstants;
import com.rp.rhopartner.util.PartnerUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class userAuthController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IEmailService emailService;

    @GetMapping("/otp")
    public ResponseEntity<Response> getOtp(@RequestParam("email")
                                                  String email) {

        com.rp.rhopartner.document.OtpResponse data = userService.getOtpByEmail(email);

        return ResponseEntity.ok(Response.builder().status(Constants.STATUS_SUCCESS).success(SuccessInfo.builder()
                .coreData(CoreData.builder().responseData(data)
                        .build())
                    .build())
                .build());
    }


    @GetMapping("/validation")
    public ResponseEntity<Response> userValidation(@RequestParam("mobileNumber")
                                                  @Valid
                                                  @Size(min = 10, max = 10, message = "Please enter your 10 digit phone number")
                                                  @Pattern(regexp = "^[0-9]*$", message = "Please enter your 10 digit phone number")
                                                  String mobileNo,
                                              @RequestParam("otp")
                                                  @Valid
                                                  @Size(min = 4, max = 4, message = "Please enter your 4 digit OTP.")
                                                  @Pattern(regexp = "^[0-9]*$", message = "Please enter your 4 digit OTP.")
                                                  String otp) {
        ValidationDetail response = userService.validateOtpForUser(mobileNo,otp);

        if(Objects.isNull(response.getMobileNo())){
            return ResponseEntity.ok(Response.builder()
                            .status(Constants.STATUS_FAIL)
                            .failure(FailureInfo.builder()
                                    .errCode(GlobalConstants.ERROR_CODE)
                                    .errStatus(GlobalConstants.ERROR_STATUS)
                                    .errMessage(GlobalConstants.OTP_ERROR_MSG)
                                    .errDescription(GlobalConstants.OTP_ERROR_DESC)
                                    .build())
                    .build());
        }

        return ResponseEntity.ok(Response.builder()
                        .status(Constants.STATUS_SUCCESS)
                        .success(SuccessInfo.builder()
                                .execTimestamp(Constants.getCurrentTimestamp())
                                .successCode(Constants.SUCCESS_CODE)
                                .coreData(CoreData.builder()
                                        .responseData(response)
                                        .build())
                                .build())
                .build());
    }

    @GetMapping("/validation/email")
    public ResponseEntity<Response> userEmailValidation(@RequestParam("email")
                                                   String email,
                                                   @RequestParam("otp")
                                                   @Valid
                                                   @Size(min = 4, max = 4, message = "Please enter your 4 digit OTP.")
                                                   @Pattern(regexp = "^[0-9]*$", message = "Please enter your 4 digit OTP.")
                                                   String otp) {
        ValidationDetail response = userService.validateOtpForUser(email,otp);
        if(response.getIsRegister() == null){
            return ResponseEntity.ok(Response.builder()
                            .status(Constants.STATUS_FAIL)
                            .failure(FailureInfo.builder()
                                    .errCode(GlobalConstants.ERROR_CODE)
                                    .errStatus(GlobalConstants.ERROR_STATUS)
                                    .errMessage(GlobalConstants.OTP_ERROR_MSG)
                                    .errDescription(GlobalConstants.OTP_ERROR_DESC)
                                    .build())
                    .build());
        }


        return ResponseEntity.ok(Response.builder()
                .status(Constants.STATUS_SUCCESS)
                .success(SuccessInfo.builder()
                        .execTimestamp(Constants.getCurrentTimestamp())
                        .successCode(Constants.SUCCESS_CODE)
                        .coreData(CoreData.builder()
                                .responseData(response)
                                .build())
                        .build())
                .build());
    }

    @GetMapping("/validation/oauth")
    public ResponseEntity<Response> userValidation(@RequestParam("email") String email) {
        ValidationDetail response = userService.validateEmailForUser(email);

        return ResponseEntity.ok(Response.builder()
                .status(Constants.STATUS_SUCCESS)
                .success(SuccessInfo.builder()
                        .execTimestamp(Constants.getCurrentTimestamp())
                        .successCode(Constants.SUCCESS_CODE)
                        .coreData(CoreData.builder()
                                .responseData(response)
                                .build())
                        .build())
                .build());
    }

    @PostMapping("/registration")
    ResponseEntity<Response> userRegistration(@RequestBody Partner partner){
        if(!PartnerUtils.isValidPartner(partner)){
            return ResponseEntity.ok(Response.builder()
                            .status(Constants.STATUS_FAIL)
                            .failure(FailureInfo.builder()
                                    .errCode(GlobalConstants.ERROR_CODE)
                                    .errStatus(GlobalConstants.ERROR_STATUS)
                                    .errMessage(GlobalConstants.REGISTRATION_ERROR_MSG)
                                    .errDescription(GlobalConstants.REGISTRATION_ERROR_DESC)
                                    .build())
                    .build());

        }
        return ResponseEntity.ok(Response.builder()
                        .status(Constants.STATUS_SUCCESS)
                        .success(SuccessInfo.builder()
                                .successCode(Constants.SUCCESS_CODE).execTimestamp(Constants.getCurrentTimestamp())
                                .coreData(CoreData.builder().responseData(userService.registerNewPartner(partner)).build())
                                .build())
                .build());
    }


    @PutMapping("/refresh")
    public ResponseEntity<Response> refreshAccessToken(@Valid @RequestParam("partnerId") String partnerId,@Valid @RequestParam("refreshToken") String refreshToken) {

        AccessToken accessToken = userService.refreshAccessToken(refreshToken,partnerId);
        if(accessToken.getAccessToken().isEmpty()){
            return ResponseEntity.ok(Response.builder()
                            .status(Constants.STATUS_FAIL)
                            .failure(FailureInfo.builder()
                                    .errCode(GlobalConstants.ERROR_CODE)
                                    .errStatus(GlobalConstants.ERROR_STATUS)
                                    .errMessage(GlobalConstants.REGISTRATION_ERROR_MSG)
                                    .errDescription(GlobalConstants.REFRESH_TOKEN_DESC)
                                    .build())
                    .build());
        }
        return ResponseEntity.ok(
                Response.builder()
                        .status(Constants.STATUS_SUCCESS)
                        .success(SuccessInfo.builder()
                                .coreData(CoreData.builder().responseData(accessToken).build())
                                .successCode(Constants.SUCCESS_CODE)
                                .execTimestamp(Constants.getCurrentTimestamp())
                                .build())
                        .build());

    }

    @PutMapping("/logout")
    public ResponseEntity<Response> logout(@Valid @RequestParam("userId") String userId, @Valid @RequestParam("refreshToken") String refreshToken) {

        if(userService.partnerLogout(refreshToken)){  // Token deleted successfully..
            return ResponseEntity.ok(Response.builder()
                    .status(Constants.STATUS_SUCCESS)
                    .success(SuccessInfo.builder()
                            .execTimestamp(Constants.getCurrentTimestamp())
                            .successCode(Constants.SUCCESS_CODE)
                            .coreData(CoreData.builder().responseData(Logout.builder()
                                            .isLogoutSuccessful(Boolean.TRUE)
                                            .logoutMessage(GlobalConstants.LOGOUT_SUCCESS).build())
                                    .build())
                            .build())
                    .build());
        }
        return ResponseEntity.ok(Response.builder()
                .status(Constants.STATUS_FAIL)
                        .failure(FailureInfo.builder()
                                .errCode(GlobalConstants.ERROR_CODE)
                                .errStatus(GlobalConstants.ERROR_STATUS)
                                .errDescription(GlobalConstants.LOGOUT_ERROR)
                                .errMessage(GlobalConstants.LOGOUT_ERROR)
                                .build())
                .build());

    }

    @GetMapping("/partner/detail")
    public ResponseEntity<Response> getPartnerDetail(@Valid @RequestParam("partnerId") String partnerId){

        return ResponseEntity.ok(Response.builder()
                        .status(Constants.STATUS_SUCCESS)
                        .success(SuccessInfo.builder()
                                .execTimestamp(Constants.getCurrentTimestamp())
                                .successCode(Constants.SUCCESS_CODE)
                                .coreData(CoreData.builder()
                                        .responseData(userService.getPartnerInfo(partnerId))
                                        .build())
                                .build())
                .build());
    }

    @PostMapping("/partner/update")
    public ResponseEntity<Response> updatePartnerDetail(@Valid @RequestBody PartnerDetail partnerDetail){

        Boolean updFlag = userService.updatePartnerInfo(partnerDetail);

        return ResponseEntity.ok(Response.builder()
                        .status(Constants.STATUS_SUCCESS)
                        .success(SuccessInfo.builder()
                                .successCode(Constants.SUCCESS_CODE)
                                .execTimestamp(Constants.getCurrentTimestamp())
                                .coreData(CoreData.builder()
                                        .responseData(PartnerUpdate.builder()
                                                .isUpdated(updFlag)
                                                .message("Partner details update status :"+updFlag)
                                                .build())
                                        .build())
                                .build())
                .build());
    }

//    @DeleteMapping("/delete")
//    public ResponseEntity<Response> deleteUser(@RequestParam("partnerId") String partnerId) {
//        // todo...
//        return ResponseEntity.ok(Response.builder().build());
//    }

}
