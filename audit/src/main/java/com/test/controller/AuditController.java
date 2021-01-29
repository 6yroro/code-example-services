package com.test.controller;

import com.test.database.entity.AuditRecord;
import com.test.services.AuditRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Alexander Zubkov
 */
@RestController
public class AuditController {

    private final AuditRecordService auditRecordService;

    @Autowired
    public AuditController(AuditRecordService auditRecordService) {
        this.auditRecordService = auditRecordService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<AuditRecord>> getAuditRecordList() {
        return ResponseEntity.ok(auditRecordService.getRecords());
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<AuditRecord> addAuditRecord(@RequestBody AuditRecord record) {
        return new ResponseEntity<>(auditRecordService.saveRecord(record), HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<AuditRecord> getAuditRecord(@PathVariable("id") Long id) {
        return ResponseEntity.ok(auditRecordService.getRecord(id));
    }

}
