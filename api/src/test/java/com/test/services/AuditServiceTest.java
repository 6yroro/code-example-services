package com.test.services;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jms.UncategorizedJmsException;
import org.springframework.jms.core.JmsTemplate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Alexander Zubkov
 */
public class AuditServiceTest {

    private JmsTemplate jmsTemplate;
    private AuditService auditService;
    private UncategorizedJmsException jmsException;

    private void sendMessage(int n) {
        String test = "test";
        for (int i = 0; i < n; i++) {
            auditService.sendMessage(test, test, test, test);
        }
    }

    public AuditServiceTest() {
        jmsException = new UncategorizedJmsException("Error");
    }

    @Before
    public void setUp() {
        jmsTemplate = mock(JmsTemplate.class);
        auditService = new AuditService(jmsTemplate);
    }

    @Test
    public void checkSendingMessage() {
        doNothing().when(jmsTemplate).convertAndSend(any(String.class), any(Object.class));
        sendMessage(1);
    }

    @Test
    public void checkSendingMessageJmsIsFailing() {
        doThrow(jmsException).when(jmsTemplate).convertAndSend(any(String.class), any(Object.class));
        sendMessage(1);
        assertEquals(1, auditService.getUnsentMessageList().size());
    }

    @Test
    public void checkSendingMessagesJmsIsFailing() {
        doThrow(jmsException).when(jmsTemplate).convertAndSend(any(String.class), any(Object.class));
        sendMessage(2);
        assertEquals(2, auditService.getUnsentMessageList().size());
    }

    @Test
    public void checkSendingUnsentMessages() {
        doThrow(jmsException).when(jmsTemplate).convertAndSend(any(String.class), any(Object.class));
        sendMessage(2);
        doNothing().when(jmsTemplate).convertAndSend(any(String.class), any(Object.class));
        sendMessage(1);
        assertEquals(0, auditService.getUnsentMessageList().size());
    }

}