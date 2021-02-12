package com.test.services;

import com.test.database.repository.AuthUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * @author Alexander Zubkov
 */
@Configuration
public class AuthUserRegistrationValidationTestConfiguration {

    @Bean
    public AuthUserService authUserService(AuthenticationManager authenticationManager, AuthUserRepository authUserRepository,
                                           PasswordEncoder passwordEncoder, TokenService tokenService) {
        return new AuthUserService(authenticationManager, authUserRepository, passwordEncoder, tokenService, "", 0L);
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

}
