package com.rp.rhopartner.repository;

import com.rp.rhopartner.document.OtpDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpDocRepo extends MongoRepository<OtpDoc, String> {
    OtpDoc findByEmail(String email);
}
