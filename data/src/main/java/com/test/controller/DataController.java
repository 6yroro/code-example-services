package com.test.controller;

import com.test.database.entity.DataEntity;
import com.test.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Alexander Zubkov
 */
@RestController
public class DataController {

    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<DataEntity>> getDataList() {
        return ResponseEntity.ok(dataService.getDataList());
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<DataEntity> getData(@PathVariable("id") Long id) {
        return ResponseEntity.ok(dataService.getData(id));
    }

}
