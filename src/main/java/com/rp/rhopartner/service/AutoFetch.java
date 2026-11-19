package com.rp.rhopartner.service;


import com.rp.rhopartner.model.BankDetail;
import com.rp.rhopartner.model.PincodeDetail;

public interface AutoFetch {
    BankDetail fetchBankDetail(String ifsc);
    PincodeDetail fetchLocationDetailByPincod(String pincode);
}
