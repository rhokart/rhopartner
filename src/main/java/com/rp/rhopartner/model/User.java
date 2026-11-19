package com.rp.rhopartner.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "user_mgmt")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private String _id;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String primaryMob;
    private String secondaryMob;
    private Address address;
    private Boolean isProfileCompleted;
    private BusinessDetail businessDetail;
    private List<Role> roles;
}
