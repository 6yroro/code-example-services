package com.test.services;

import com.test.database.entity.AuthUser;
import com.test.model.AuthenticationRequest;
import com.test.model.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Alexander Zubkov
 */
@Service
public class AuthUserAuditService {

    private final AuditService auditService;
    private final AuthUserService authUserService;

    @Autowired
    public AuthUserAuditService(AuditService auditService, AuthUserService authUserService) {
        this.auditService = auditService;
        this.authUserService = authUserService;
    }

    private String getUsername() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return "";
        return attributes.getRequest().getHeader("Username");
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String result = null;
        AuthenticationResponse response;
        try {
            response = authUserService.authenticate(request);
            result = "Success: " + response;
        } catch (Exception e) {
            result = "Fail: " + e.getMessage();
            throw e;
        } finally {
            auditService.sendMessage(
                    request.getUsername(),
                    "User Authentication",
                    request.toString(),
                    result
            );
        }

        return response;
    }

    public AuthUser register(AuthenticationRequest request) {
        String result = null;
        AuthUser authUser;
        try {
            authUser = authUserService.register(request);
            result = "Success: " + authUser.toString();
        } catch (Exception e) {
            result = "Fail: " + e.getMessage();
            throw e;
        } finally {
            auditService.sendMessage(
                    getUsername(),
                    "User Registration",
                    request.toString(),
                    result
            );
        }

        return authUser;
    }

}
