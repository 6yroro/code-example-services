package com.test.security;

import com.test.services.TokenService;
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
import java.util.stream.Collectors;

/**
 * @author Alexander Zubkov
 */
@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final String tokenHeader;
    private final String tokenPrefix;
    private final String secret;

    public AuthenticationTokenFilter(TokenService tokenService) {
        this.tokenService = tokenService;
        this.tokenHeader = "Authorization";
        this.tokenPrefix = "Bearer ";
        this.secret = "SECRET_KEY_FOR_TESTING_SPRING_SECURITY";
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain)throws IOException, ServletException {
        String header = request.getHeader(tokenHeader);

        if (header != null && header.startsWith(tokenPrefix)) {
            String token = header.replace(tokenPrefix, "");

            try {
                tokenService.validate(token, secret);

                String username = tokenService.getUsername();
                if (username != null) {
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(username, null,
                                    tokenService.getAuthorities().stream()
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
