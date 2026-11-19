package com.rp.rhopartner.repository;

import com.rp.rhopartner.document.SellerPincode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PincodesRepository extends MongoRepository<SellerPincode, String> {
}
