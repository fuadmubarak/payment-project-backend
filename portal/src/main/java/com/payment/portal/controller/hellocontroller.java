package com.payment.portal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class hellocontroller {
    
    @GetMapping("/")
    public String home() {
        return "Portal backend is running 🚀";
    }
 
    @GetMapping("/health")
    public String health() {
        return "OK";
    }

}
