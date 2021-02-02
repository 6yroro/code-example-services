package com.test.messages;

import com.test.database.entity.AuditRecord;
import com.test.model.AuditMessage;
import com.test.services.AuditRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Zubkov
 */
@Component
public class AuditMessageReceiver {

    private static final Logger log = LoggerFactory.getLogger(AuditMessageReceiver.class);

    private final AuditRecordService auditRecordService;

    @Autowired
    public AuditMessageReceiver(AuditRecordService auditRecordService) {
        this.auditRecordService = auditRecordService;
    }

    @JmsListener(destination = "audit", containerFactory = "jmsFactory")
    public void message(AuditMessage message) {
        log.info("Message: " + message.toString());
        AuditRecord auditRecord = new AuditRecord();
        auditRecord.setUsername(message.getUsername());
        auditRecord.setAction(message.getAction());
        auditRecord.setParams(message.getParams());
        auditRecord.setResult(message.getResult());
        auditRecord.setDate(message.getDate());
        auditRecordService.saveRecord(auditRecord);
    }

}
