package com.test.services;

import com.test.database.entity.AuthUser;
import com.test.database.repository.AuthUserRepository;
import com.test.exceptions.UserExistException;
import com.test.model.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * @author Alexander Zubkov
 */
@Service
@PropertySource("classpath:token.properties")
@Validated
public class AuthUserService {

    private final static String USER_AUTHORITY = "USER";

    private final AuthenticationManager authenticationManager;
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final String secret;
    private final Long duration;

    @Autowired
    public AuthUserService(AuthenticationManager authenticationManager, AuthUserRepository authUserRepository,
                           PasswordEncoder passwordEncoder, TokenService tokenService,
                           @Value("${secret}") String secret,
                           @Value("${duration}") Long duration) {
        this.authenticationManager = authenticationManager;
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.secret = secret;
        this.duration = duration;
    }

    String authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenService.getToken(authentication, secret, duration);
    }

    AuthUser register(@Valid AuthenticationRequest request) {
        AuthUser user = authUserRepository
                .findAuthUserByUsername(request.getUsername())
                .orElse(null);

        if (user != null) {
            throw new UserExistException("User " + request.getUsername() + " already exist");
        }

        AuthUser newUser = new AuthUser();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setAuthorities(request.getAuthorities() != null ? request.getAuthorities() : USER_AUTHORITY);

        return authUserRepository.save(newUser);
    }

}
