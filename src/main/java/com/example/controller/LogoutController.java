package com.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @Value("${spring.security.oauth2.client.registration.auth0.client-id}")
    private String clientId;

    @GetMapping("/logout")
    public String logout() {
        // Spring will handle local logout, then redirect to Auth0's logout
        String returnTo = "http://localhost:8080";
        String logoutUrl = "https://dev-l5f3fqnk4y5amjwt.us.auth0.com/v2/logout?client_id=" + clientId
                + "&returnTo=" + returnTo;
        return "redirect:" + logoutUrl;
    }
}