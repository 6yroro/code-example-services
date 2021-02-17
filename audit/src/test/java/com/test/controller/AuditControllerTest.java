package com.test.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.database.entity.AuditRecord;
import com.test.services.AuditRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Alexander Zubkov
 */
@RunWith(SpringRunner.class)
@WebMvcTest
@SuppressWarnings("ConstantConditions")
public class AuditControllerTest {

    @MockBean
    private AuditRecordService auditRecordService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    private final String rootPath, wrongPath, idPath;
    private final String rootJsonPath, idJsonPath, usernameJsonPath;
    private final String test;
    private final AuditRecord record1, record2, record3;
    private final List<AuditRecord> allRecords;

    public AuditControllerTest() {
        rootPath = "/";
        wrongPath = "/wrong/path";
        idPath = "/{id}";
        rootJsonPath = "$";
        idJsonPath = "$.id";
        usernameJsonPath = "$.username";
        test = "test1";
        String test2 = "test2";
        String test3 = "test3";
        record1 = new AuditRecord(1L, test, test, test, test, LocalDateTime.now());
        record2 = new AuditRecord(2L, test2, test2, test2, test2, LocalDateTime.now());
        record3 = new AuditRecord(3L, test3, test3, test3, test3, LocalDateTime.now());
        allRecords = Arrays.asList(record1, record2, record3);
    }

    private <T> T getResponseContent(MvcResult result, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);
    }

    @Test
    public void getWrongPath() throws Exception {
        mvc.perform(get(wrongPath))
                .andExpect(status().isNotFound());
    }

    @Test
    public void postWrongPath() throws Exception {
        mvc.perform(post(wrongPath))
                .andExpect(status().isNotFound());
    }

    @Test
    public void putRootPath() throws Exception {
        mvc.perform(put(rootPath))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(result -> assertEquals(HttpRequestMethodNotSupportedException.class, result.getResolvedException().getClass()));
    }

    @Test
    public void getRootPathEmptyRecordList() throws Exception {
        mvc.perform(get(rootPath))
                .andExpect(status().isOk())
                .andExpect(jsonPath(rootJsonPath).isEmpty());
    }

    @Test
    public void getRootPathRecordList() throws Exception {
        when(auditRecordService.getRecords()).thenReturn(allRecords);
        mvc.perform(get(rootPath))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals(allRecords,
                        getResponseContent(result, new TypeReference<List<AuditRecord>>() {})));
    }

    @Test
    public void getRootPathServiceError() throws Exception {
        when(auditRecordService.getRecords()).thenThrow(new RuntimeException());
        mvc.perform(get(rootPath))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(RuntimeException.class, result.getResolvedException().getClass()));
    }

    @Test
    public void postRootPathEmptyRequest() throws Exception {
        mvc.perform(post(rootPath))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(HttpMessageNotReadableException.class, result.getResolvedException().getClass()));
    }

    @Test
    public void postRootPathAuditRecordNullResult() throws Exception {
        mvc.perform(post(rootPath)
                        .content(objectMapper.writeValueAsString(record1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(rootJsonPath).doesNotExist());
    }

    @Test
    public void postRootPathAuditRecordResult() throws Exception {
        when(auditRecordService.saveRecord(record2)).thenReturn(record2);
        mvc.perform(post(rootPath)
                .content(objectMapper.writeValueAsString(record2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(idJsonPath).value(record2.getId()))
                .andExpect(jsonPath(usernameJsonPath).value(record2.getUsername()));
    }

    @Test
    public void postRootPathServiceError() throws Exception {
        when(auditRecordService.saveRecord(record3)).thenThrow(new RuntimeException());
        mvc.perform(post(rootPath)
                .content(objectMapper.writeValueAsString(record3))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(RuntimeException.class, result.getResolvedException().getClass()));
    }

    @Test
    public void postIdPathRecord() throws Exception {
        mvc.perform(post(idPath, record1.getId()))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getIdPathEmptyId() throws Exception {
        mvc.perform(get(idPath)).andReturn();
    }

    @Test
    public void getIdPathWrongType() throws Exception {
        mvc.perform(get(idPath, test))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(MethodArgumentTypeMismatchException.class, result.getResolvedException().getClass()));
    }

    @Test
    public void getIdPathRecordNullResult() throws Exception {
        mvc.perform(get(idPath, record1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath(rootJsonPath).doesNotExist());
    }

    @Test
    public void getIdPathRecordNotFoundResult() throws Exception {
        when(auditRecordService.getRecord(record2.getId())).thenThrow(new NoSuchElementException());
        mvc.perform(get(idPath, record2.getId()))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(NoSuchElementException.class, result.getResolvedException().getClass()));
    }

    @Test
    public void getIdPathRecordResult() throws Exception {
        when(auditRecordService.getRecord(record3.getId())).thenReturn(record3);
        mvc.perform(get(idPath, record3.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath(idJsonPath).value(record3.getId()))
                .andExpect(jsonPath(usernameJsonPath).value(record3.getUsername()));
    }

    @Test
    public void getIdPathRecordServiceError() throws Exception {
        when(auditRecordService.getRecord(record2.getId())).thenThrow(new RuntimeException());
        mvc.perform(get(idPath, record2.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(RuntimeException.class, result.getResolvedException().getClass()));
    }

}