package com.test.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final String tokenHeader;
    private final String tokenPrefix;
    private final String Secret;

    public AuthenticationTokenFilter() {
        tokenHeader = "X-Auth-Token";
        tokenPrefix = "Bearer ";
        Secret = "SECRET_KEY_FOR_TESTING_SPRING_SECURITY";
    }

    @Override

    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain)throws IOException, ServletException {
        String header = request.getHeader(tokenHeader);

        if (header != null && header.startsWith(tokenPrefix)) {
            String token = header.replace(tokenPrefix, "");

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(Secret.getBytes(Charset.forName("UTF-8"))))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                if (username != null) {
                    @SuppressWarnings("unchecked")
                    List<String> authorities = (List<String>) claims.get("authorities");
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(username, null,
                                    authorities.stream()
                                            .map(SimpleGrantedAuthority::new)
                                            .collect(Collectors.toList())));
                }
            } catch (Exception e) {
                e.printStackTrace();
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }

}
