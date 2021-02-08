package com.test.database.repository;

import com.test.database.entity.AuditRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author Alexander Zubkov
 */
public interface AuditRecordRepository extends CrudRepository<AuditRecord, Long> {

    @NonNull
    List<AuditRecord> findAll();

}
