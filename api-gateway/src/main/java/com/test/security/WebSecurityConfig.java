package com.test.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private final String authPath;
    private final String registerPath;
    private final String auditPath;
    private final String dataPath;
    private final String adminRole;
    private final String[] dataRoles;
    private final String[] auditRoles;

    @Autowired
    public WebSecurityConfig(AuthenticationTokenFilter authenticationTokenFilter,
                             UsernameToRequestFilter usernameToRequestFilter,
                             @Value("${routes.auth}") String authPath,
                             @Value("${routes.register}") String registerPath,
                             @Value("${zuul.routes.audit-service.path}") String auditPath,
                             @Value("${zuul.routes.data-service.path}") String dataPath,
                             @Value("${authority.admin}") String adminRole,
                             @Value("${authority.data}") String[] dataRoles,
                             @Value("${authority.audit}") String[] auditRoles) {
        this.authenticationTokenFilter = authenticationTokenFilter;
        this.usernameToRequestFilter = usernameToRequestFilter;
        this.authPath = authPath;
        this.registerPath = registerPath;
        this.auditPath = auditPath;
        this.dataPath = dataPath;
        this.adminRole = adminRole;
        this.dataRoles = dataRoles;
        this.auditRoles = auditRoles;
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
                .antMatchers(HttpMethod.POST, authPath).permitAll()
                .antMatchers(HttpMethod.POST, registerPath).hasAuthority(adminRole)
                .antMatchers(dataPath).hasAnyAuthority(dataRoles)
                .antMatchers(auditPath).hasAnyAuthority(auditRoles)
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
