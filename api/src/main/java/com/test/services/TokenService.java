package com.test.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexander Zubkov
 */
@Service
public class TokenService {

    private Claims claims;

    private final String authorities = "authorities";
    private final Charset charset = Charset.forName("UTF-8");

    String getToken(Authentication authentication, String secret, long duration) {
        Long now = System.currentTimeMillis();

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(authorities, authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + duration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(charset)))
                .compact();
    }

    public void validate(String token, String secret) {
        claims = null;
        claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(charset)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsername() {
        return claims.getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getAuthorities() {
        return (List<String>) claims.get(authorities);
    }



}
