package com.payment.portal.service;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class TokenService {
    public String generate() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}