package com.test.services;

import com.test.model.AuditMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Alexander Zubkov
 */
@Service
public class AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditService.class);

    private final JmsTemplate jmsTemplate;
    private final String destination;

    private List<AuditMessage> unsentMessageList;

    @Autowired
    public AuditService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
        this.destination = "audit";
        unsentMessageList = new LinkedList<>();
    }

    private void sendUnsentMessage(String destination) {
        if (unsentMessageList.isEmpty()) return;

        Iterator<AuditMessage> iterator = unsentMessageList.iterator();
        while (iterator.hasNext()) {
            AuditMessage message = iterator.next();
            try {
                jmsTemplate.convertAndSend(destination, message);
                iterator.remove();
            } catch (Exception e) {
                log.error("Audit message can not be resend", e);
            }
        }
    }

    void sendMessage(String username, String action, String params, String result) {
        AuditMessage auditMessage = new AuditMessage();
        auditMessage.setUsername(username);
        auditMessage.setAction(action);
        auditMessage.setParams(params);
        auditMessage.setResult(result);
        auditMessage.setDate(LocalDateTime.now());
        try {
            jmsTemplate.convertAndSend(destination, auditMessage);
            sendUnsentMessage(destination);
        } catch (Exception e) {
            unsentMessageList.add(auditMessage);
            log.error("Audit message can not be send", e);
        }
    }

}
