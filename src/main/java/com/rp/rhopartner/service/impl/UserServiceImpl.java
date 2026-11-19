package com.rp.rhopartner.service.impl;

import com.rp.rhopartner.document.OtpDoc;
import com.rp.rhopartner.document.OtpResponse;
import com.rp.rhopartner.document.RefreshToken;
import com.rp.rhopartner.dto.*;
import com.rp.rhopartner.exception.UserNotFound;
import com.rp.rhopartner.model.EmailDetails;
import com.rp.rhopartner.model.PartnerInfo;
import com.rp.rhopartner.repository.DbRepository;
import com.rp.rhopartner.repository.OtpDocRepo;
import com.rp.rhopartner.service.*;
import com.rp.rhopartner.util.GlobalConstants;
import com.rp.rhopartner.util.PartnerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import static com.rp.rhopartner.util.GlobalConstants.*;

@Slf4j
@Component
public class UserServiceImpl implements IUserService {

    @Autowired
    private OtpDocRepo docRepo;

    @Autowired
    private OtpProcessingService processingService;

    @Autowired
    private DbRepository dbRepository;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private OtpMongoDbImpl iOtpUtils;

    @Autowired
    private OtpDocRepo otpDocRepo;

    @Autowired
    private IEmailService emailService;

    @Override
    public Boolean sendOtpSms(String mobileNo) {

        if (mobileNo.length() == 10) {
            // default otp is store 1234 for normal login.
            // we need to keep send logic here...
//            processingService.generateOtp(mobileNo,GlobalConstants.OTP_LENGTH);  // This code will be open once we integrate with sms gateway.

            OtpDoc sampleOtp = new OtpDoc(mobileNo, "1234", GlobalConstants.OTP_VALIDITY, System.currentTimeMillis());
            docRepo.save(sampleOtp);
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public OtpResponse getOtpByEmail(String email) {

        OtpDoc gotp = new OtpDoc(email, iOtpUtils.generateOtp(email, OTP_LENGTH), OTP_VALIDITY, System.currentTimeMillis());
        OtpDoc rotp = otpDocRepo.save(gotp);

        EmailDetails details = EmailDetails.builder()
                .msgBody("Your one time login password for Rhokart is :"+gotp.getOtp()+".\n"+"Thank you for choosing Rhokart.")
                .subject("Alert: Rhokart Otp for login.")
                .toAddress(email.trim())
                .build();

        if(!emailService.sendEmailToUser(details)) {
            log.error("Email send fail..");
            throw new UserNotFound("User email send fail","Not able to send email to user");
        }
        com.rp.rhopartner.document.OtpResponse otpResponse = new com.rp.rhopartner.document.OtpResponse();
        otpResponse.setEmail(rotp.getEmail());
        otpResponse.setValidityInSecond(rotp.getValidityInSecond());
        otpResponse.setGenerationTimeStamp(rotp.getGenerationTimeStamp());
        return otpResponse;
    }

    @Override
    public ValidationDetail validateOtpForUser(String email, String otp) {
        if (!processingService.validateOtp(email, otp)) {
            log.info("Incorrect login process..");
            return ValidationDetail.builder().build();
        }
        boolean isRegistered = true;

        PartnerInfo partnerInfo = dbRepository.selectPartNerInfoByEmail(email);
        if (Objects.isNull(partnerInfo)) {
            partnerInfo = PartnerInfo.builder()
                    .email(email)
                    .build();
            isRegistered = false;
            return ValidationDetail.builder()
                    .isRegister(isRegistered)
                    .email(email)
                    .build();
        }
        RefreshToken refreshToken = RefreshToken.builder().id(partnerInfo.getPartnerId()).partnerInfo(partnerInfo).build();
        refreshToken.setCreatedBy(GlobalConstants.ADMIN);
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshTokenRepo.save(refreshToken);
        Date loginTime = new Date();
        String accessToken = jwtHelper.generateAccessToken(partnerInfo.getPartnerId(), loginTime);
        String generateRefreshToken = jwtHelper.generateRefreshToken(partnerInfo.getPartnerId(), refreshToken.getId());

        return ValidationDetail.builder()
                .isRegister(isRegistered)
                .partnerId(Objects.requireNonNullElse(partnerInfo.getPartnerId(), ""))
                .accessToken(accessToken)
                .mobileNo(partnerInfo.getMobileNo())
                .email(email)
                .refreshToken(generateRefreshToken)
                .build();
    }

    @Override
    public ValidationDetail validateEmailForUser(String email) {
        boolean isRegistered = true;

        PartnerInfo partnerInfo = dbRepository.selectPartNerInfoByEmail(email);
        if (Objects.isNull(partnerInfo)) {
            isRegistered = false;
            return ValidationDetail.builder().isRegister(isRegistered).email(email).build();
        }
        RefreshToken refreshToken = RefreshToken.builder().id(partnerInfo.getPartnerId()).partnerInfo(partnerInfo).build();
        refreshToken.setCreatedBy(GlobalConstants.ADMIN);
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshTokenRepo.save(refreshToken);
        Date loginTime = new Date();
        String accessToken = jwtHelper.generateAccessToken(partnerInfo.getPartnerId(), loginTime);
        String generateRefreshToken = jwtHelper.generateRefreshToken(partnerInfo.getPartnerId(), refreshToken.getId());

        return ValidationDetail.builder()
                .isRegister(isRegistered)
                .partnerId(Objects.requireNonNullElse(partnerInfo.getPartnerId(), ""))
                .accessToken(accessToken)
                .mobileNo(partnerInfo.getMobileNo())
                .email(email)
                .refreshToken(generateRefreshToken)
                .build();
    }

    @Override
    public RegisterDetail registerNewPartner(Partner partner) {
        PartnerInfo info = generatePartnerInfo(partner);
        // registering partner information for fresh login.
        dbRepository.insertPartnerInfoToDb(info);
        RefreshToken refreshToken = RefreshToken.builder().id(info.getPartnerId()).partnerInfo(info).build();
        refreshToken.setCreatedBy(GlobalConstants.ADMIN);
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshTokenRepo.save(refreshToken);
        Date loginTime = new Date();
        String accessToken = jwtHelper.generateAccessToken(info.getPartnerId(), loginTime);
        String generateRefreshToken = jwtHelper.generateRefreshToken(info.getPartnerId(), refreshToken.getId());
        // returning partner id as response.
        return RegisterDetail.builder()
                .email(info.getEmail())
                .fname(info.getFname())
                .lname(info.getLname())
                .partnerId(info.getPartnerId())
                .mobile(info.getAltMobileNo())
                .accessToken(accessToken)
                .refreshToken(generateRefreshToken)
                .build();
    }

    public AccessToken refreshAccessToken(String refreshToken, String partnerId){
        PartnerInfo user = dbRepository.fetchPartNerInfoByPartnerId(partnerId);
        if(Objects.isNull(user)){
            return AccessToken.builder()
                    .userId(partnerId)
                    .accessToken(GlobalConstants.EMPTY_VALUE)
                    .build();
        }
        if (jwtHelper.validateRefreshToken(refreshToken) && refreshTokenRepo.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshToken))) {
            RefreshToken storedToken =  refreshTokenRepo.findById(jwtHelper.getTokenIdFromRefreshToken(refreshToken)).orElse(null);

            if(Objects.isNull(storedToken)){
                return AccessToken.builder()
                        .userId(partnerId)
                        .accessToken(GlobalConstants.EMPTY_VALUE)
                        .build();
            }
            log.info("Valid refresh token, sending access token.");
            return AccessToken.builder()
                    .userId(partnerId)
                    .accessToken(jwtHelper.generateAccessToken(partnerId,new Date()))
                    .build();
        }
        log.error("refresh token is invalid.. please try with different token. {}", refreshToken);
        return AccessToken.builder()
                .userId(partnerId)
                .accessToken(GlobalConstants.EMPTY_VALUE)
                .build();
    }

    public Boolean partnerLogout(String refreshToken) {
        if (jwtHelper.validateRefreshToken(refreshToken) && refreshTokenRepo.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshToken))) {
            refreshTokenRepo.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshToken));
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public PartnerDetail getPartnerInfo(String partnerId) {
        PartnerInfo partnerInfo = dbRepository.fetchPartNerInfoByPartnerId(partnerId);
        return PartnerUtils.partnerDetailsMapper(partnerInfo);
    }

    @Override
    public Boolean updatePartnerInfo(PartnerDetail detail) {
        log.info("state:{}",detail.getState());
        dbRepository.updatePartnerDetail(detail.getPartnerId(), detail.getFname(), detail.getLname(),
                detail.getMobileNo(), detail.getAltMobileNo(), detail.getEmail(), detail.getDob(), detail.getAddress(), detail.getPinCode(),
                detail.getDistrict(), detail.getCity(), detail.getState(), detail.getUpiId(), detail.getAccHolderName(),
         detail.getAccType(), detail.getAccNumber(), detail.getIfscCode(), detail.getBankName(), detail.getBranchName(),
                detail.getPanNumber(), detail.getPanUrl(), detail.getAadharNumber(), detail.getAadharUrl());

        return Boolean.TRUE;
    }


    private PartnerInfo generatePartnerInfo(Partner partner) {
        // Generate partner Id
        return PartnerInfo.builder()
                .partnerId(String.valueOf(Instant.now().toEpochMilli()))   // unique id is assign to partner everytime a new partner is register.
                .mobileNo(partner.getMobileNo())
                .altMobileNo(partner.getAltMobileNo())
                .fname(partner.getFname())
                .lname(partner.getLname())
                .email(partner.getEmail())
                .dob(partner.getDob())
                .address(partner.getAddress())
                .city(partner.getCity())
                .pinCode(partner.getPinCode())
                .district(partner.getDistrict())
                .state(partner.getState())
                .accHolderName(partner.getAccHolderName())
                .accNumber(partner.getAccNumber())
                .accType(partner.getAccType())
                .ifscCode(partner.getIfscCode())
                .bankName(partner.getBankName())
                .branchName(partner.getBranchName())
                .upiId(partner.getUpiId())
                .aadharNumber(partner.getAadharNumber())
                .aadharUrl(partner.getAadharUrl())
                .panNumber(partner.getPanNumber())
                .panUrl(partner.getPanUrl())
                .build();
    }
}