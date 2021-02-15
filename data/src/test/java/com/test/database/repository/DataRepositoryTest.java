package com.test.database.repository;

import com.test.database.entity.DataEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Alexander Zubkov
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class DataRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private DataRepository dataRepository;

    private DataEntity data1, data2;
    private List<DataEntity> dataList;

    public DataRepositoryTest() {
        data1 = new DataEntity();
        data1.setCode("1");
        data1.setName("1");
        data2 = new DataEntity();
        data2.setCode("2");
        data2.setName("2");
        dataList = Arrays.asList(data1, data2);
    }

    @Test
    public void getAllEmpty() {
        assertEquals(Collections.emptyList(), dataRepository.findAll());
    }

    @Test
    public void getAll() {
        dataList.forEach(entityManager::persist);
        assertEquals(dataList, dataRepository.findAll());
    }

    @Test
    public void getNotExist() {
        assertEquals(Optional.empty(), dataRepository.findById(1L));
    }

    @Test
    public void getExist() {
        entityManager.persist(data1);
        assertEquals(Optional.of(data1), dataRepository.findById(data1.getId()));
    }

    @Test
    public void save() {
        assertEquals(dataRepository.save(data2), entityManager.find(DataEntity.class, data2.getId()));
    }

    @Test
    public void saveAll() {
        dataRepository.saveAll(dataList);
        List<DataEntity> saved = new ArrayList<>();
        dataList.forEach(data -> saved.add(entityManager.find(DataEntity.class, data.getId())));
        assertEquals(dataList, saved);
    }

}