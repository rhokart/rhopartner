package com.rp.rhopartner.document;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "rhopartner_otpdoc")
public class OtpDoc {
    @Id
    private String email;
    private String otp;
    private long validityInSecond;
    private long generationTimeStamp;

}
