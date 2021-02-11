package com.test.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author Alexander Zubkov
 */
@Data
public class AuthenticationRequest {

    @NotBlank(message = "is mandatory")
    @Size(min = 3, max = 100)
    private String username;

    @NotBlank(message = "is mandatory")
    @Size(min = 3, max = 200)
    private String password;

    @Max(100)
    private String authorities;

}
