package com.test.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.springframework.jms.core.JmsTemplate;

/**
 * @author Alexander Zubkov
 */
public class AuditServiceTest {

    private JmsTemplate jmsTemplate;
    private AuditService auditService;

    private void sendMessage(int n) {
        String test = "test";
        for (int i = 0; i < n; i++) {
            auditService.sendMessage(test, test, test, test);
        }
    }

    @Before
    public void setUp() {
        jmsTemplate = Mockito.mock(JmsTemplate.class);
        auditService = new AuditService(jmsTemplate);
    }

    @Test
    public void checkSendingMessage() {
        Mockito.doNothing().when(jmsTemplate).convertAndSend(Mockito.any(String.class), Mockito.any(Object.class));
        sendMessage(1);
    }

    @Test
    public void checkSendingMessageJmsIsFailing() {
        Mockito.doThrow(new MockitoException("Error")).when(jmsTemplate)
                .convertAndSend(Mockito.any(String.class), Mockito.any(Object.class));
        sendMessage(1);
        Assert.assertEquals(1, auditService.getUnsentMessageList().size());
    }

    @Test
    public void checkSendingMessagesJmsIsFailing() {
        Mockito.doThrow(new MockitoException("Error")).when(jmsTemplate)
                .convertAndSend(Mockito.any(String.class), Mockito.any(Object.class));
        sendMessage(2);
        Assert.assertEquals(2, auditService.getUnsentMessageList().size());
    }

    @Test
    public void checkSendingUnsentMessages() {
        Mockito.doThrow(new MockitoException("Error")).when(jmsTemplate)
                .convertAndSend(Mockito.any(String.class), Mockito.any(Object.class));
        sendMessage(2);
        Mockito.doNothing().when(jmsTemplate).convertAndSend(Mockito.any(String.class), Mockito.any(Object.class));
        sendMessage(1);
        Assert.assertEquals(0, auditService.getUnsentMessageList().size());
    }

}