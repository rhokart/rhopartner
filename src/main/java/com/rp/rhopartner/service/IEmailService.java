package com.rp.rhopartner.service;

import com.rp.rhopartner.model.EmailDetails;
import org.springframework.stereotype.Service;

@Service
public interface IEmailService {
    boolean sendEmailToUser(EmailDetails details);
}
