package com.test.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexander Zubkov
 */
@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationTokenFilter.class);

    private final String tokenHeader;
    private final String tokenPrefix;
    private final String Secret;

    public AuthenticationTokenFilter() {
        tokenHeader = "X-Auth-Token";
        tokenPrefix = "Bearer ";
        Secret = "SECRET_KEY_FOR_TESTING_SPRING_SECURITY";
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(tokenHeader);

        log.info("header = " + header);

        if (header != null && header.startsWith(tokenPrefix)) {
            String token = header.replace(tokenPrefix, "");

            log.info("token = " + token);

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(Secret.getBytes(Charset.forName("UTF-8"))))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                log.info("username = " + username);
                if (username != null) {
                    @SuppressWarnings("unchecked")
                    List<String> authorities = (List<String>) claims.get("authorities");
                    log.info("authorities = " + authorities);
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(username, null,
                                    authorities.stream()
                                            .map(SimpleGrantedAuthority::new)
                                            .collect(Collectors.toList())));
                }
            } catch (Exception e) {
                log.info("error", e);
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }

}
