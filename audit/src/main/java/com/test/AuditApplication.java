package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.jms.annotation.EnableJms;

/**
 * @author Alexander Zubkov
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableJms
public class AuditApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuditApplication.class, args);
    }

}
