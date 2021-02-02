package com.test.services;

import com.test.database.entity.AuthUser;
import com.test.model.AuditMessage;
import com.test.model.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Alexander Zubkov
 */
@Service
public class AuthenticationAuditService {

    private final JmsTemplate jmsTemplate;
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationAuditService(JmsTemplate jmsTemplate, AuthenticationService authenticationService) {
        this.jmsTemplate = jmsTemplate;
        this.authenticationService = authenticationService;
    }

    private void sendAuditMessage(AuditMessage auditMessage) {
        jmsTemplate.convertAndSend("audit", auditMessage);
    }

    public String authenticate(AuthenticationRequest request) {
        AuditMessage auditMessage = new AuditMessage();
        auditMessage.setUsername(request.getUsername());
        auditMessage.setAction("User Authentication");
        auditMessage.setParams(request.toString());
        String token;
        try {
            token = authenticationService.authenticate(request);
            auditMessage.setResult("Success: " + token);
            auditMessage.setDate(LocalDateTime.now());
            sendAuditMessage(auditMessage);
        } catch (Exception e) {
            auditMessage.setResult("Fail: " + e.getMessage());
            auditMessage.setDate(LocalDateTime.now());
            sendAuditMessage(auditMessage);
            throw e;
        }

        return token;
    }

    public AuthUser registerUser(AuthenticationRequest request, String username) {
        AuditMessage auditMessage = new AuditMessage();
        auditMessage.setUsername(username);
        auditMessage.setAction("User Registration");
        auditMessage.setParams(request.toString());
        AuthUser authUser;
        try {
            authUser = authenticationService.registerUser(request);
            auditMessage.setResult("Success: " + authUser.toString());
            auditMessage.setDate(LocalDateTime.now());
            sendAuditMessage(auditMessage);
        } catch (Exception e) {
            auditMessage.setResult("Fail: " + e.getMessage());
            auditMessage.setDate(LocalDateTime.now());
            sendAuditMessage(auditMessage);
            throw e;
        }

        return authUser;
    }

}
