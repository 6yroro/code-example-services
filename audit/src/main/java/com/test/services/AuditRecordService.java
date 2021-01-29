package com.test.services;

import com.test.database.entity.AuditRecord;
import com.test.database.repository.AuditRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Alexander Zubkov
 */
@Service
public class AuditRecordService {

    private final AuditRecordRepository auditRecordRepository;

    @Autowired
    public AuditRecordService(AuditRecordRepository auditRecordRepository) {
        this.auditRecordRepository = auditRecordRepository;
    }

    public List<AuditRecord> getRecords() {
        return auditRecordRepository.findAll();
    }

    public AuditRecord getRecord(Long id) {
        return auditRecordRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Audit record with id = " + id + " not exists"));
    }

    public AuditRecord saveRecord(AuditRecord record) {
        AuditRecord newRecord = new AuditRecord();
        newRecord.setUser(record.getUser());
        newRecord.setAction(record.getAction());
        newRecord.setResult(record.getResult());
        newRecord.setDate(record.getDate());
        return auditRecordRepository.save(newRecord);
    }

}
