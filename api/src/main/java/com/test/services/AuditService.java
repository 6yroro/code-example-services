package com.test.services;

import com.test.model.AuditMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Alexander Zubkov
 */
@Service
public class AuditService {

    private final JmsTemplate jmsTemplate;
    private final String destination;

    @Autowired
    public AuditService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
        this.destination = "audit";
    }

    void sendAuditMessage(String username, String action, String params, String result) {
        AuditMessage auditMessage = new AuditMessage();
        auditMessage.setUsername(username);
        auditMessage.setAction(action);
        auditMessage.setParams(params);
        auditMessage.setResult(result);
        auditMessage.setDate(LocalDateTime.now());
        jmsTemplate.convertAndSend(destination, auditMessage);
    }

}
