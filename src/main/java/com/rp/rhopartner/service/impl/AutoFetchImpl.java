package com.rp.rhopartner.service.impl;

import com.rp.rhopartner.document.SellerPincode;
import com.rp.rhopartner.model.BankDetail;
import com.rp.rhopartner.model.IfscApiResponse;
import com.rp.rhopartner.model.PincodeDetail;
import com.rp.rhopartner.repository.PincodesRepository;
import com.rp.rhopartner.service.AutoFetch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AutoFetchImpl implements AutoFetch {

    private final RestTemplate restTemplate;

    @Autowired
    private PincodesRepository pincodesRepository;

    @Value("${rapidapi.key}")
    private String rapidApiKey;

    @Value("${rapidapi.ifsc.url}")
    private String ifscApiUrl;


    @Override
    public BankDetail fetchBankDetail(String ifsc) {
        String url = ifscApiUrl + ifsc;

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", rapidApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<IfscApiResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, IfscApiResponse.class);
            IfscApiResponse apiResponse = response.getBody();
            if (apiResponse == null) {
                return BankDetail.builder()
                        .ifscCode(ifsc)
                        .isIFscVerified(false)
                        .build();
            }

            return BankDetail.builder()
                    .ifscCode(apiResponse.getIfsc())
                    .bankName(apiResponse.getBank())
                    .branchName(apiResponse.getBranch())
                    .branchCityName(apiResponse.getCity())
                    .isIFscVerified(true)
                    .build();
        } catch (Exception ex) {
            // Handle 404 or other exceptions
            return BankDetail.builder()
                    .ifscCode(ifsc)
                    .isIFscVerified(false)
                    .build();
        }
    }

    @Override
    public PincodeDetail fetchLocationDetailByPincod(String pincode) {
        Optional<SellerPincode> code = pincodesRepository.findById(pincode);

        if(code.isPresent()){
            return PincodeDetail.builder()
                    .country(code.get().getCountry())
                    .cityName(code.get().getCity())
                    .state(code.get().getState())
                    .districtName(code.get().getDistrict())
                    .pincode(code.get().getPincode())
                    .build();
        }

        return PincodeDetail.builder().build();
    }
}