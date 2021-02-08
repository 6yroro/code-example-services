package com.test.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Alexander Zubkov
 */
@Configuration
@ConfigurationProperties(prefix = "token")
@Getter
@Setter
public class TokenConfig {

    private String header;
    private String prefix;
    private String secret;

}
