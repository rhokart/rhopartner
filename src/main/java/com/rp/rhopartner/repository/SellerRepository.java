package com.rp.rhopartner.repository;

import com.rp.rhopartner.model.SellerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends MongoRepository<SellerDocument, String> {

    @Query(value = "{ '_id': ?0 }", fields = "{ 'sellerName': 1, 'email': 1, 'mobileNo': 1 }")
    SellerDocument findSellerDetailsById(String id);

}