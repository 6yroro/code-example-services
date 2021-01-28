package com.test.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Alexander Zubkov
 */
@RestController
public class AuditController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getAuditRecordList() {
        return ResponseEntity.ok("Audit record list");
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addAuditRecord() {
        return new ResponseEntity<>("New audit record created", HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getAuditRecord(@PathVariable("id") Long id) {
        return ResponseEntity.ok("Audit record #" + id);
    }

}
