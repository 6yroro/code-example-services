package com.test.services;

import com.test.model.AuthUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alexander Zubkov
 */
@Service
@Primary
public class AuthUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(AuthUserDetailsService.class);

    private final List<AuthUser> users;

    @Autowired
    public AuthUserDetailsService(PasswordEncoder passwordEncoder) {
        users = Arrays.asList(
                new AuthUser("user", passwordEncoder.encode("user"), "USER"),
                new AuthUser("admin", passwordEncoder.encode("admin"), "ADMIN")
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser findUser = users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> {
                    String message = "User with username: " + username + " not found";
                    log.warn(message);
                    return new UsernameNotFoundException(message);
                });

        return new User(
                findUser.getUsername(),
                findUser.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(findUser.getAuthorities())
        );
    }

}
