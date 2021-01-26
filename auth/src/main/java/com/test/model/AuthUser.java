package com.test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Alexander Zubkov
 */
@Data
@Builder
@AllArgsConstructor
public class AuthUser {

    private String username;
    private String password;
    private String authorities;

}
