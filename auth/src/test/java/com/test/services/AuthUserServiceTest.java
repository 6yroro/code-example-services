package com.test.services;

import com.test.database.entity.AuthUser;
import com.test.database.repository.AuthUserRepository;
import com.test.exceptions.UserExistException;
import com.test.model.AuthenticationRequest;
import com.test.model.AuthenticationResponse;
import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Alexander Zubkov
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthUserServiceTest {

    @Mock private AuthenticationManager authenticationManager;
    @Mock private AuthUserRepository authUserRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private TokenService tokenService;
    private AuthUserService authUserService;
    private HibernateException exception;

    public AuthUserServiceTest() {
        exception = new HibernateException("");
    }

    @Before
    public void setUp() {
        authUserService = new AuthUserService(authenticationManager, authUserRepository,
                passwordEncoder, tokenService, "", 0L);
    }

    @Test(expected = NullPointerException.class)
    public void checkAuthenticateNullRequest() {
        authUserService.authenticate(null);
    }

    @Test(expected = NullPointerException.class)
    public void checkAuthenticateEmptyRequest() {
        authUserService.authenticate(new AuthenticationRequest());
    }

    @Test(expected = NullPointerException.class)
    public void checkAuthenticateEmptyRequestToken() {
        String token = "test";
        when(tokenService.getToken(any(), any(), anyLong())).thenReturn(token);
        authUserService.authenticate(new AuthenticationRequest());
    }

    @Test
    public void checkAuthenticateEmptyRequestTokenAuthority() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE1"));
        authorities.add(new SimpleGrantedAuthority("ROLE2"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("", null, authorities);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        String token = "test";
        when(tokenService.getToken(eq(authentication), any(), anyLong())).thenReturn(token);
        List<String> resultAuthorities = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        assertEquals(new AuthenticationResponse(token, resultAuthorities),
                authUserService.authenticate(new AuthenticationRequest()));
    }

    @Test(expected = BadCredentialsException.class)
    public void checkAuthenticateBadCredentials() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException(""));
        authUserService.authenticate(new AuthenticationRequest());
    }

    @Test(expected = RuntimeException.class)
    public void checkAuthenticateTokenException() {
        when(tokenService.getToken(any(), any(), anyLong())).thenThrow(new RuntimeException());
        authUserService.authenticate(new AuthenticationRequest());
    }

    @Test(expected = NullPointerException.class)
    public void checkRegisterNullRequest() {
        authUserService.register(null);
    }

    @Test
    public void checkRegisterEmptyRequest() {
        authUserService.register(new AuthenticationRequest());
    }

    @Test
    public void checkRegisterNewUser() {
        when(authUserRepository.findAuthUserByUsername(any())).thenReturn(Optional.empty());
        authUserService.register(new AuthenticationRequest());
    }

    @Test(expected = UserExistException.class)
    public void checkRegisterExistUser() {
        when(authUserRepository.findAuthUserByUsername(any())).thenReturn(Optional.of(new AuthUser()));
        authUserService.register(new AuthenticationRequest());
    }

    @Test
    public void checkRegisterNewUserSave() {
        AuthUser authUser = new AuthUser();
        when(authUserRepository.save(any())).thenReturn(authUser);
        assertEquals(authUser, authUserService.register(new AuthenticationRequest()));
    }

    @Test
    public void checkRegisterNewUserDefaultAuthority() {
        AuthUser authUser = new AuthUser();
        authUser.setAuthorities("USER");
        when(authUserRepository.save(authUser)).thenReturn(authUser);
        assertEquals(authUser, authUserService.register(new AuthenticationRequest()));
    }

    @Test
    public void checkRegisterNewUserAuthority() {
        AuthUser authUser = new AuthUser();
        authUser.setAuthorities("AUDIT");
        when(authUserRepository.save(authUser)).thenReturn(authUser);
        AuthenticationRequest request = new AuthenticationRequest();
        request.setAuthorities("AUDIT");
        assertEquals(authUser, authUserService.register(request));
    }

    @Test(expected = HibernateException.class)
    public void checkRegisterDBErrorFind() {
        when(authUserRepository.findAuthUserByUsername(any())).thenThrow(exception);
        authUserService.register(new AuthenticationRequest());
    }

    @Test(expected = HibernateException.class)
    public void checkRegisterDBErrorSave() {
        when(authUserRepository.save(any())).thenThrow(exception);
        authUserService.register(new AuthenticationRequest());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkRegisterEncoderError() {
        when(passwordEncoder.encode(any())).thenThrow(new IllegalArgumentException());
        authUserService.register(new AuthenticationRequest());
    }

}