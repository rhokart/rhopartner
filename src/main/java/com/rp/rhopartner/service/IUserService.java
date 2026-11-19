package com.rp.rhopartner.service;

import com.rp.rhopartner.document.OtpResponse;
import com.rp.rhopartner.dto.*;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserService {
    Boolean sendOtpSms(String mobileNumber);
    OtpResponse getOtpByEmail(String email);
    ValidationDetail validateOtpForUser(String mobileNumber, String otp);
    ValidationDetail validateEmailForUser(String email);
    RegisterDetail registerNewPartner(Partner partner);
    AccessToken refreshAccessToken(String refreshToken, String partnerId);
    Boolean partnerLogout(String refreshToken);
    PartnerDetail getPartnerInfo(String partnerId);
    Boolean updatePartnerInfo(PartnerDetail detail);
}
