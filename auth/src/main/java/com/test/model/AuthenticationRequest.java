package com.test.model;

import lombok.Data;

/**
 * @author Alexander Zubkov
 */
@Data
public class AuthenticationRequest {

    private String username;
    private String password;

}
