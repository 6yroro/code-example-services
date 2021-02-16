package com.test.aop;

import com.test.database.entity.DataEntity;
import com.test.database.repository.DataRepository;
import com.test.services.AuditService;
import com.test.services.DataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * @author Alexander Zubkov
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AuditAspectTestConf.class)
public class AuditAspectTest {

    @MockBean
    private AuditService auditService;
    @MockBean
    private DataRepository dataRepository;

    @Autowired
    private DataService dataService;

    private final String dataListAction, dataAction, empty, username, error;
    private final DataEntity data1;

    public AuditAspectTest() {
        dataListAction = "Getting data list";
        dataAction = "Getting data";
        empty = "";
        username = "Test";
        error = "Error";
        data1 = new DataEntity();
        data1.setId(1L);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Username", username);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void getEmptyDataList() {
        dataService.getDataList();
        verify(auditService, only()).sendMessage(
                eq(username),
                eq(dataListAction),
                eq(empty),
                eq("Success: []")
        );
    }

    @Test
    public void getDataList() {
        DataEntity data2 = new DataEntity();
        data2.setId(2L);
        DataEntity data3 = new DataEntity();
        data3.setId(3L);
        when(dataRepository.findAll()).thenReturn(Arrays.asList(data1, data2, data3));
        dataService.getDataList();
        verify(auditService, only()).sendMessage(
                eq(username),
                eq(dataListAction),
                eq(empty),
                eq("Success: [1, 2, 3]")
        );
    }

    @Test
    public void getDataListError() {
        when(dataRepository.findAll()).thenThrow(new RuntimeException(error));
        try {
            dataService.getDataList();
        } catch (Exception ignored) {}
        verify(auditService, only()).sendMessage(
                eq(username),
                eq(dataListAction),
                eq(empty),
                eq("Fail: " + error)
        );
    }

    @Test
    public void getNotExistData() {
        try {
            dataService.getData(data1.getId());
        } catch (Exception ignored) {}
        verify(auditService, only()).sendMessage(
                eq(username),
                eq(dataAction),
                eq("id = 1"),
                eq("Fail: Data with id = 1 not found")
        );
    }

    @Test
    public void getDataError() {
        when(dataRepository.findById(anyLong())).thenThrow(new RuntimeException(error));
        try {
            dataService.getData(data1.getId());
        } catch (Exception ignored) {}
        verify(auditService, only()).sendMessage(
                eq(username),
                eq(dataAction),
                eq("id = 1"),
                eq("Fail: " + error)
        );
    }

    @Test
    public void getExistData() {
        when(dataRepository.findById(anyLong())).thenReturn(Optional.of(data1));
        dataService.getData(2L);
        verify(auditService, only()).sendMessage(
                eq(username),
                eq(dataAction),
                eq("id = 2"),
                eq("Success: DataEntity(id=1, code=null, name=null, description=null, date=null, username=null)")
        );
    }

}