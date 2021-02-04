package com.test.services;

import com.test.database.entity.AuthUser;
import com.test.database.repository.AuthUserRepository;
import com.test.exceptions.UserExistException;
import com.test.model.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Zubkov
 */
@Service
public class AuthUserService {

    private final AuthenticationManager authenticationManager;
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    private final static String USER_AUTHORITY = "USER";
    private final String secret;

    @Autowired
    public AuthUserService(AuthenticationManager authenticationManager, AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        secret = "SECRET_KEY_FOR_TESTING_SPRING_SECURITY";
    }

    String authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenService.getToken(authentication, secret);
    }

    AuthUser register(AuthenticationRequest request) {
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
