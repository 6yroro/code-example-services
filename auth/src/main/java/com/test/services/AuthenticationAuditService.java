package com.test.services;

import com.test.database.entity.AuthUser;
import com.test.model.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Zubkov
 */
@Service
public class AuthenticationAuditService {

    private final AuditService auditService;
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationAuditService(AuditService auditService, AuthenticationService authenticationService) {
        this.auditService = auditService;
        this.authenticationService = authenticationService;
    }

    public String authenticate(AuthenticationRequest request) {
        String result = null;
        String token;
        try {
            token = authenticationService.authenticate(request);
            result = "Success: " + token;
        } catch (Exception e) {
            result = "Fail: " + e.getMessage();
            throw e;
        } finally {
            auditService.sendAuditMessage(
                    request.getUsername(),
                    "User Authentication",
                    request.toString(),
                    result
            );
        }

        return token;
    }

    public AuthUser registerUser(AuthenticationRequest request, String username) {
        String result = null;
        AuthUser authUser;
        try {
            authUser = authenticationService.registerUser(request);
            result = "Success: " + authUser.toString();
        } catch (Exception e) {
            result = "Fail: " + e.getMessage();
            throw e;
        } finally {
            auditService.sendAuditMessage(
                    username,
                    "User Registration",
                    request.toString(),
                    result
            );
        }

        return authUser;
    }

}
