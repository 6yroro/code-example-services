package com.test.services;

import com.test.database.entity.DataEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexander Zubkov
 */
@Service
public class DataAuditService {

    private final AuditService auditService;
    private final DataService dataService;

    public DataAuditService(AuditService auditService, DataService dataService) {
        this.auditService = auditService;
        this.dataService = dataService;
    }

    private String getUsername() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return "";
        return attributes.getRequest().getHeader("Username");
    }

    public List<DataEntity> getDataList() {
        String result = null;
        List<DataEntity> dataList;
        try {
            dataList = dataService.getDataList();
            result = "Success: " + dataList.stream()
                    .map(DataEntity::getId)
                    .collect(Collectors.toList())
                    .toString();
        } catch (Exception e) {
            result = "Fail: " + e.getMessage();
            throw e;
        } finally {
            auditService.sendMessage(
                    getUsername(),
                    "Getting data list",
                    null,
                    result
            );
        }

        return dataList;
    }

    public DataEntity getData(Long id) {
        String result = null;
        DataEntity data;
        try {
            data = dataService.getData(id);
            result = "Success: " + data.toString();
        } catch (Exception e) {
            result = "Fail: " + e.getMessage();
            throw e;
        } finally {
            auditService.sendMessage(
                    getUsername(),
                    "Getting data",
                    id.toString(),
                    result
            );
        }

        return data;
    }

}
