package com.example.config;

import com.example.util.EnvironmentUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration
public class OAuth2ClientConfig {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration auth0Registration = ClientRegistration.withRegistrationId("auth0")
                .clientId(EnvironmentUtils.getEnv("AUTH0_CLIENT_ID")) // Fetch client ID using utility
                .clientSecret(EnvironmentUtils.getEnv("AUTH0_CLIENT_SECRET")) // Fetch client secret
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