package com.test.services;

import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author Alexander Zubkov
 */
public class TokenServiceTest {

    private TokenService tokenService;
    private String role;
    private String username;
    private String secret;
    private long duration;
    private Authentication authentication;

    @Before
    public void setUp() {
        tokenService = new TokenService();
        role = "TEST_ROLE";
        username = "TEST";
        secret = "TEST_SECRET_KEY_FOR_TESTING_SPRING_SECURITY";
        duration = 600000;
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    @Test
    public void checkTokenCreation() {
        String token = tokenService.getToken(authentication, secret, duration);
        assertNotNull(token);
    }

    @Test(expected = NullPointerException.class)
    public void checkTokenCreationAuthenticationIsNull() {
        tokenService.getToken(null, secret, duration);
    }

    @Test(expected = WeakKeyException.class)
    public void checkTokenCreationSecretIsSmall() {
        tokenService.getToken(authentication, "ABC", duration);
    }

    @Test(expected = NullPointerException.class)
    public void checkTokenCreationSecretIsNull() {
        tokenService.getToken(authentication, null, duration);
    }

    @Test
    public void checkTokenValidation() {
        String token = tokenService.getToken(authentication, secret, duration);
        tokenService.validate(token, secret);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkTokenValidationTokenIsNull() {
        tokenService.validate(null, secret);
    }

    @Test(expected = SignatureException.class)
    public void checkTokenValidationTokenIsNotSame() {
        String token = tokenService.getToken(authentication, secret, duration);
        tokenService.validate(token + "_", secret);
    }

    @Test(expected = NullPointerException.class)
    public void checkTokenValidationSecretIsNull() {
        String token = tokenService.getToken(authentication, secret, duration);
        tokenService.validate(token, null);
    }

    @Test(expected = SignatureException.class)
    public void checkTokenValidationSecretIsNotSame() {
        String token = tokenService.getToken(authentication, secret, duration);
        tokenService.validate(token, secret + "_");
    }

    @Test
    public void checkTokenUsername() {
        String token = tokenService.getToken(authentication, secret, duration);
        tokenService.validate(token, secret);
        assertEquals(username, tokenService.getUsername());
    }

    @Test
    public void checkTokenAuthority() {
        String token = tokenService.getToken(authentication, secret, duration);
        tokenService.validate(token, secret);
        assertEquals(Collections.singletonList(role), tokenService.getAuthorities());
    }

    @Test
    public void checkTokenAuthorities() {
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(role),
                new SimpleGrantedAuthority("USER_ROLE"));
        String token = tokenService.getToken(
                new UsernamePasswordAuthenticationToken(username, null, authorities),
                secret, duration);
        tokenService.validate(token, secret);
        assertEquals(authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()),
                tokenService.getAuthorities());
    }
}