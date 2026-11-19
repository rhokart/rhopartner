package com.rp.rhopartner.util;

import java.util.Arrays;
import java.util.List;

public class GlobalConstants {

    private GlobalConstants(){}
    public static final String ADMIN = "ADMIN";
    public static final long OTP_VALIDITY = 50;
    public static final int OTP_LENGTH = 4;
    public static final Integer ZERO = 0;
    public static final Integer ONE = 1;
    public static final Integer SEVEN_DAYS = 7;
    public static final String EARNED = "EARNED";
    public static final String REDEEMED = "REDEEMED";
    public static final String PENDING = "PENDING";
    public static final String OTP_ERROR_MSG = "Incorrect Otp Send.";
    public static final String OTP_ERROR_DESC = "Incorrect OTP, correct and send again.";
    public static final int ERROR_CODE = 8;
    public static final int ERROR_STATUS = 2080;
    public static final String REGISTRATION_ERROR_MSG = "Incorrect partner details.";
    public static final String REGISTRATION_ERROR_DESC = "Incorrect partner detail, please correct and send again.";
    public static final String REFRESH_TOKEN_DESC = "Refresh token is incorrect , please correct and send again.";
    public static final String EMPTY_VALUE = "";
    public static final String LOGOUT_SUCCESS = "Partner logout successfully.";
    public static final String LOGOUT_ERROR = "Partner logout fail!.";
    public static final List<String> VALID_ORDER_STATUS = Arrays.asList("DELIVER","COMPLETE","ORDERED");
    public static final String EARNED_DESC = "Earned transactions by the user..";
    public static final String EARNED_MSG = "Earned transactions by the user..";
    public static final String EMPTY_STRING = "";
    public static final String SYSUSER = "SYSUSER";
    public static final String PAID = "PAID";

}
