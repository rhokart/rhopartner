package com.rp.rhopartner.service;


import com.rp.rhopartner.document.OtpDoc;
import com.rp.rhopartner.repository.OtpDocRepo;
import com.rp.rhopartner.util.GlobalConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
@Slf4j
public class OtpProcessingService {

    @Autowired
    private OtpDocRepo otpDocRepo;


    public String generateOtp(String mobNumber, int len) {
        String validNumber = "0123456789";
        Random random = new Random();
        StringBuilder otp = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            otp.append(validNumber.charAt(random.nextInt(validNumber.length())));

        log.info("generateOtp--  Mob={},Opt={}", mobNumber, otp);
        return otp.toString();
    }


    public boolean validateOtp(String email, String oneTimePass) {
        if (email == null || oneTimePass == null)
            return Boolean.FALSE;

        OtpDoc retrivePassword = otpDocRepo.findByEmail(email);
        log.info("For Mobile No ::{}, Otp Object ::{}", email, retrivePassword);
        if (retrivePassword == null)
            return Boolean.FALSE;

        long currentTime = System.currentTimeMillis();
        long diffTime = (currentTime - retrivePassword.getGenerationTimeStamp()) / 1000;
        log.info("time validity remaining for login otp::{}", diffTime);

        if (oneTimePass.equalsIgnoreCase(retrivePassword.getOtp())) {
            if ((diffTime <= GlobalConstants.OTP_VALIDITY)) {
                otpDocRepo.delete(retrivePassword);
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
}
