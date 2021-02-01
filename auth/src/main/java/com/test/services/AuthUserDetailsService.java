package com.test.services;

import com.test.database.entity.AuthUser;
import com.test.database.repository.AuthUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Zubkov
 */
@Service
@Primary
public class AuthUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(AuthUserDetailsService.class);

    private final AuthUserRepository authUserRepository;

    @Autowired
    public AuthUserDetailsService(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser user = authUserRepository
                .findAuthUserByUsername(username)
                .orElseThrow(() -> {
                    String message = "User with username: " + username + " not found";
                    log.warn(message);
                    return new UsernameNotFoundException(message);
                });

        return new User(
                user.getUsername(),
                user.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAuthorities())
        );
    }

}
