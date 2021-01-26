package com.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Alexander Zubkov
 */
@Data
@AllArgsConstructor
public class AuthenticationResponse {

    private String token;

}
