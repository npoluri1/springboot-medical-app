package com.example.controller;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    private static final Logger logger = LoggerFactory.getLogger(LogoutController.class);
    private final Dotenv dotenv = Dotenv.configure().load(); // Load environment variables
    private final String clientId = dotenv.get("AUTH0_CLIENT_ID"); // Get client ID from .env or environment
    private final String clientSecret = dotenv.get("AUTH0_CLIENT_SECRET"); // Get client secret from .env or environment

    public LogoutController() {
        // Log the environment variables (for debugging purposes)
        logger.debug("AUTH0_CLIENT_ID: {}", clientId);
        logger.debug("AUTH0_CLIENT_SECRET: {}", clientSecret);
    }

    @GetMapping("/logout")
    public String logout() {
        // Spring will handle local logout, then redirect to Auth0's logout
        String returnTo = "http://localhost:8080";
        String logoutUrl = "https://dev-l5f3fqnk4y5amjwt.us.auth0.com/v2/logout?client_id=" + clientId
                + "&returnTo=" + returnTo;
        return "redirect:" + logoutUrl;
    }
}