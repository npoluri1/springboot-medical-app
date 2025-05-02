package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

@Configuration
public class OAuth2ClientConfig {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration auth0Registration = ClientRegistration.withRegistrationId("auth0")
                .clientId("i2ZwGpIFW3zljhRbLgQPpe7fg5xqcXdc")
                .clientSecret("o4JCCW3paBGYQOT8JdiyB9mUwiUNTqs3mox8Cposs6oN8IQX9RVdcXXkraautwdb")
                .scope("openid", "profile", "email")
                .authorizationUri("https://dev-l5f3fqnk4y5amjwt.us.auth0.com/authorize")
                .tokenUri("https://dev-l5f3fqnk4y5amjwt.us.auth0.com/oauth/token")
                .userInfoUri("https://dev-l5f3fqnk4y5amjwt.us.auth0.com/userinfo")
                .jwkSetUri("https://dev-l5f3fqnk4y5amjwt.us.auth0.com/.well-known/jwks.json")
                .userNameAttributeName("sub")
                .clientName("Auth0")
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .authorizationGrantType(org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE)
                .build();

        return new InMemoryClientRegistrationRepository(auth0Registration);
    }
}