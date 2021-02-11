package com.test.services;

import com.test.database.entity.AuditRecord;
import com.test.database.repository.AuditRecordRepository;
import org.hibernate.HibernateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Alexander Zubkov
 */
@RunWith(MockitoJUnitRunner.class)
public class AuditRecordServiceTest {

    @Mock
    private AuditRecordRepository auditRecordRepository;
    @InjectMocks
    private AuditRecordService auditRecordService;
    private AuditRecord record1, record2, record3;
    private List<AuditRecord> allRecords;
    private HibernateException exception;

    public AuditRecordServiceTest() {
        String test = "test";
        record1 = new AuditRecord(1L, test, test, test, test, LocalDateTime.now());
        record2 = new AuditRecord(2L, test, test, test, test, LocalDateTime.now());
        record3 = new AuditRecord(3L, test, test, test, test, LocalDateTime.now());
        allRecords = Arrays.asList(record1, record2, record3);
        exception = new HibernateException("Error");
    }

    @Test
    public void checkGettingEmptyAuditRecords() {
        assertEquals(new ArrayList<>(), auditRecordService.getRecords());
    }

    @Test
    public void checkGettingAllAuditRecords() {
        when(auditRecordRepository.findAll()).thenReturn(allRecords);
        assertEquals(allRecords, auditRecordService.getRecords());
    }

    @Test(expected = HibernateException.class)
    public void checkGettingAuditRecordsDBError() {
        when(auditRecordRepository.findAll()).thenThrow(exception);
        auditRecordService.getRecords();
    }

    @Test
    public void checkGettingAuditRecord1() {
        when(auditRecordRepository.findById(1L)).thenReturn(Optional.of(record1));
        assertEquals(record1, auditRecordService.getRecord(1L));
    }

    @Test
    public void checkGettingAuditRecord2() {
        when(auditRecordRepository.findById(2L)).thenReturn(Optional.of(record2));
        assertEquals(record2, auditRecordService.getRecord(2L));
    }

    @Test(expected = NoSuchElementException.class)
    public void checkGettingMissedAuditRecord() {
        auditRecordService.getRecord(1L);
    }

    @Test(expected = HibernateException.class)
    public void checkGettingAuditRecordDBError() {
        when(auditRecordRepository.findById(3L)).thenThrow(exception);
        auditRecordService.getRecord(3L);
    }

    @Test
    public void checkSavingAuditRecord1() {
        when(auditRecordRepository.save(record1)).thenReturn(record1);
        assertEquals(record1, auditRecordService.saveRecord(record1));
    }

    @Test
    public void checkSavingAuditRecord3() {
        when(auditRecordRepository.save(record3)).thenReturn(record3);
        assertEquals(record3, auditRecordService.saveRecord(record3));
    }

    @Test(expected = HibernateException.class)
    public void checkSavingAuditRecordDBError() {
        when(auditRecordRepository.save(record2)).thenThrow(exception);
        auditRecordService.saveRecord(record2);
    }

}