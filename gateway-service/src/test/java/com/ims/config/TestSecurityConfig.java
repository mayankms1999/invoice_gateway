package com.ims.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Answers.RETURNS_SELF;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class SecurityConfigTest {

    private final SecurityConfig securityConfig = new SecurityConfig();

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Test
    void testSecurityFilterChain() {
        assertNotNull(securityFilterChain, "SecurityFilterChain should not be null");
    }


}
