package com.rp.rhopartner.util;

import com.rp.rhopartner.dto.Partner;
import com.rp.rhopartner.dto.PartnerDetail;
import com.rp.rhopartner.model.PartnerInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Objects;

public class PartnerUtils {
    // A strong 256-bit (32-byte) Base64-encoded key
    private static final String BASE64_SECRET = "K4d9lBq7T1yXpS3zU2Rvq1+F9CmX8Lk7OcB2eJ6V0Ys=";

    public static Boolean isValidPartner(Partner partner){

        if(Objects.isNull(partner.getFname()))
            return Boolean.FALSE;

        if(Objects.isNull(partner.getLname()))
            return Boolean.FALSE;

        if(Objects.isNull(partner.getAddress()))
            return Boolean.FALSE;

        if(Objects.isNull(partner.getAccHolderName()))
            return Boolean.FALSE;

        if(Objects.isNull(partner.getAccType()))
            return Boolean.FALSE;

        if(Objects.isNull(partner.getIfscCode()))
            return Boolean.FALSE;

        if(partner.getPanNumber().length() != 10)
            return Boolean.FALSE;

        if(partner.getUpiId().isEmpty())
            return Boolean.FALSE;

        if(partner.getAadharNumber().length() !=12)
            return Boolean.FALSE;

        if(Objects.isNull(partner.getAadharUrl()))
            return Boolean.FALSE;

        if(Objects.isNull(partner.getPanUrl()))
            return Boolean.FALSE;


        return Boolean.TRUE;

    }

    public static String getInvoiceIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("invoiceId", String.class);
    }

    private static SecretKey getSigningKey() {
        byte[] decodedKey = Base64.getDecoder().decode(BASE64_SECRET);
        return new SecretKeySpec(decodedKey, SignatureAlgorithm.HS256.getJcaName());
    }

    public static PartnerDetail partnerDetailsMapper(PartnerInfo info){

        return PartnerDetail.builder()
                .partnerId(info.getPartnerId())
                .fname(info.getFname())
                .lname(info.getLname())
                .email(info.getEmail())
                .mobileNo(info.getMobileNo())
                .altMobileNo(info.getAltMobileNo())
                .address(info.getAddress())
                .dob(info.getDob())
                .state(info.getState())
                .city(info.getCity())
                .district(info.getDistrict())
                .pinCode(info.getPinCode())
                .aadharNumber(info.getAadharNumber())
                .aadharUrl(info.getAadharUrl())
                .panNumber(info.getPanNumber())
                .panUrl(info.getPanUrl())
                .accHolderName(info.getAccHolderName())
                .accNumber(info.getAccNumber())
                .accType(info.getAccType())
                .bankName(info.getBankName())
                .branchName(info.getBranchName())
                .ifscCode(info.getIfscCode())
                .upiId(info.getUpiId())
                .build();
    }
}
