package com.test.services;

import com.test.database.entity.DataEntity;
import com.test.database.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Alexander Zubkov
 */
@Service
public class DataService {

    private final DataRepository dataRepository;

    @Autowired
    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    List<DataEntity> getDataList() {
        return dataRepository.findAll();
    }

    DataEntity getData(Long id) {
        return dataRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Data with id = " + id + " not found"));
    }

}
