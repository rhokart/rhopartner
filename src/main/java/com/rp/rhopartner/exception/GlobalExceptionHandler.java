package com.rp.rhopartner.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /***
     * provides handling throughout the service here...
     */


    @ExceptionHandler(value = {UserNotFound.class})
    public ResponseEntity<ErrorMessage> userNotFound(UserNotFound ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrMessage("User is not register");
        errorMessage.setErrDescription("User is not register");
        if (!Objects.isNull((ex.getErrmessage())))
            errorMessage.setErrMessage(ex.getErrmessage());
        if (!Objects.isNull(ex.getErrDescription()))
            errorMessage.setErrDescription(ex.getErrDescription());
        errorMessage.setErrCode(15);
        errorMessage.setErrStatus(1502);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(value = {OtpVerificationFailed.class})
    public ResponseEntity<ErrorMessage> invalidOtp(OtpVerificationFailed ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrMessage("Invalid OTP");
        errorMessage.setErrDescription("OTP verification failed, Please enter OTP again");
        if (!Objects.isNull((ex.getErrorMessage())))
            errorMessage.setErrMessage(ex.getErrorMessage());
        if (!Objects.isNull(ex.getErrDescription()))
            errorMessage.setErrDescription(ex.getErrDescription());
        errorMessage.setErrCode(14);
        errorMessage.setErrStatus(1400);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotAbleSendSms.class})
    public ResponseEntity<ErrorMessage> notAbleSendSms(NotAbleSendSms ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrMessage("Not able to Send OTP");
        errorMessage.setErrDescription("OTP send fail.");
        if (!Objects.isNull((ex.getErrmessage())))
            errorMessage.setErrMessage(ex.getErrmessage());
        if (!Objects.isNull(ex.getErrDescription()))
            errorMessage.setErrDescription(ex.getErrDescription());
        errorMessage.setErrCode(17);
        errorMessage.setErrStatus(1702);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {InvalidUser.class})
    public ResponseEntity<ErrorMessage> invalidUser(InvalidUser ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrMessage("Invalid username or password");
        errorMessage.setErrDescription("User Not Found");
        if (!Objects.isNull((ex.getErrmessage())))
            errorMessage.setErrMessage(ex.getErrmessage());
        if (!Objects.isNull(ex.getErrDescription()))
            errorMessage.setErrDescription(ex.getErrDescription());
        errorMessage.setErrCode(15);
        errorMessage.setErrStatus(1502);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {InvalidAppContext.class})
    public ResponseEntity<ErrorMessage> invalidAppContext(InvalidAppContext ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrMessage("Invalid Application Context");
        errorMessage.setErrDescription("Please Check AppId or AppName.");
        if (!Objects.isNull((ex.getErrmessage())))
            errorMessage.setErrMessage(ex.getErrmessage());
        if (!Objects.isNull(ex.getErrDescription()))
            errorMessage.setErrDescription(ex.getErrDescription());
        errorMessage.setErrCode(15);
        errorMessage.setErrStatus(1501);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BadRequestData.class})
    public ResponseEntity<ErrorMessage> badRequestException(BadRequestData ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setErrMessage("Bad Request");
        errorMessage.setErrDescription("Please enter valid data in the Form.");
        if (!Objects.isNull((ex.getErrmessage())))
            errorMessage.setErrMessage(ex.getErrmessage());
        if (!Objects.isNull(ex.getErrDescription()))
            errorMessage.setErrDescription(ex.getErrDescription());
        errorMessage.setErrCode(15);
        errorMessage.setErrStatus(1501);
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


}
