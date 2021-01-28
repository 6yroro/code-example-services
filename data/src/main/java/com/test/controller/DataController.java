package com.test.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alexander Zubkov
 */
@RestController
public class DataController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<String>> getDataList() {
        return ResponseEntity.ok(Arrays.asList("One", "Two", "Three", "Four", "Five"));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Long>> getData(@PathVariable("id") Long id) {
        return ResponseEntity.ok(Arrays.asList(id, id*2, id*3, id*4, id*5));
    }

}
