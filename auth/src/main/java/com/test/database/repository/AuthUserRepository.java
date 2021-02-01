package com.test.database.repository;

import com.test.database.entity.AuthUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author Alexander Zubkov
 */
public interface AuthUserRepository extends CrudRepository<AuthUser, Long> {

    Optional<AuthUser> findAuthUserByUsername(String username);

}
