package com.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Alexander Zubkov
 */
@Data
@AllArgsConstructor
public class AuthenticationResponse {

    private String token;
    private List<String> authorities;

}
