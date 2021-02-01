package com.test.services;

import com.test.database.entity.AuthUser;
import com.test.database.repository.AuthUserRepository;
import com.test.exceptions.UserExistException;
import com.test.model.AuthenticationRequest;
import com.test.model.AuthenticationResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.sql.Date;
import java.util.stream.Collectors;

/**
 * @author Alexander Zubkov
 */
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final static String USER_AUTHORITY = "USER";
    private final String Secret;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        Secret = "SECRET_KEY_FOR_TESTING_SPRING_SECURITY";
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Long now = System.currentTimeMillis();
        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 10 * 60 * 1000))
                .signWith(Keys.hmacShaKeyFor(Secret.getBytes(Charset.forName("UTF-8"))))
                .compact();

        return new AuthenticationResponse(token);
    }

    public AuthUser registerUser(AuthenticationRequest request) {
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
