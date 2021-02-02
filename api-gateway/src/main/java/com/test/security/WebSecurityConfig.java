package com.test.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @author Alexander Zubkov
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationTokenFilter authenticationTokenFilter;
    private final UsernameToRequestFilter usernameToRequestFilter;

    @Autowired
    public WebSecurityConfig(AuthenticationTokenFilter authenticationTokenFilter, UsernameToRequestFilter usernameToRequestFilter) {
        this.authenticationTokenFilter = authenticationTokenFilter;
        this.usernameToRequestFilter = usernameToRequestFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
                .disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .exceptionHandling()
                .authenticationEntryPoint((req, rsp, e) ->
                        rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access denied"))
                .and()
            .addFilterAfter(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(usernameToRequestFilter, SecurityContextHolderAwareRequestFilter.class)
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/register").hasAuthority("ADMIN")
                .antMatchers("/data/**").hasAnyAuthority("ADMIN", "USER", "AUDIT")
                .antMatchers("/audit/**").hasAnyAuthority("ADMIN", "AUDIT")
                .anyRequest().authenticated()
                .and()
            .cors();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowedMethods(Arrays.stream(HttpMethod.values()).map(HttpMethod::name).collect(Collectors.toList()));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
