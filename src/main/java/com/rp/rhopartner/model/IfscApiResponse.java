package com.rp.rhopartner.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IfscApiResponse {
    @JsonProperty("UPI")
    private boolean upi;

    @JsonProperty("ISO3166")
    private String iso3166;

    @JsonProperty("IMPS")
    private boolean imps;
    @JsonProperty("ADDRESS")
    private String address;
    @JsonProperty("CITY")
    private String city;

    @JsonProperty("STATE")
    private String state;

    @JsonProperty("CONTACT")
    private String contact;

    @JsonProperty("BRANCH")
    private String branch;

    @JsonProperty("SWIFT")
    private String swift;

    @JsonProperty("NEFT")
    private boolean neft;

    @JsonProperty("DISTRICT")
    private String district;

    @JsonProperty("MICR")
    private String micr;

    @JsonProperty("RTGS")
    private boolean rtgs;

    @JsonProperty("CENTRE")
    private String centre;

    @JsonProperty("BANK")
    private String bank;

    @JsonProperty("BANKCODE")
    private String bankCode;

    @JsonProperty("IFSC")
    private String ifsc;
}